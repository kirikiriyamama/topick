require 'open-uri'
require 'net/http'
require 'rexml/document'

require File.join(DIR, 'lib', 'Hash')
require File.join(DIR, 'lib', 'create_url')


class Topick < Sinatra::Base
	configure :development do
		Bundler.require :development
		register Sinatra::Reloader

		require 'pp'
	end

	configure do
		set(:facebook) { YAML.load_file(FACEBOOK_CONF).symbolize_keys }
		set(:yahoo_api) { YAML.load_file(YAHOO_API_CONF).symbolize_keys }
	end
	
	def oauth_facebook
		Koala::Facebook::OAuth.new(
			settings.facebook[:app_id],
			settings.facebook[:app_secret],
			create_url(request.scheme, request.host, '/auth/facebook/callback'))
	end


	get '/topic/facebook' do
		halt 400 if params[:access_token].blank?
		halt 400 if params[:target_user].blank?
		halt 400 if params[:keyphrase].blank?

		keyphrase = String.new
		params[:keyphrase].split(',').each do |str|
			keyphrase << "(#{str})|"
		end
		keyphrase.chop!
		
		response = Array.new
		graph = Koala::Facebook::API.new(params[:access_token])
		begin
			graph.get_connections(params[:target_user], 'posts', :limit => 100).each do |result|
				topic = Hash.new
				result = result.symbolize_keys

				# message
				if !result[:message].nil? && (result[:message] =~ Regexp.new(keyphrase)) then
					topic[:summary] = result[:message].slice(0, 20)
					if (result[:type] == 'link') && (result[:status_type] == 'shared_story') then
						topic[:shared_link] = { :link => result[:link] }
						topic[:shared_link][:summary] = result[:name].slice(0, 20) unless result[:name].nil?
					end
				# shared link
				elsif (result[:type] == 'link') && (result[:status_type] == 'shared_story') then
					if (!result[:name].nil? && (result[:name] =~ Regexp.new(keyphrase))) || (!result[:description].nil? && (result[:description] =~ Regexp.new(keyphrase))) then
						topic[:shared_link] = { :link => result[:link] }
						topic[:shared_link][:summary] = result[:name].slice(0, 20) unless result[:name].nil?
					end
				end

				# create response
				unless topic.blank? then
					id = result[:id].split('_')
					topic[:link] = create_url('http', Koala::Facebook::DIALOG_HOST, "/#{id.first}/posts/#{id.last}")
					topic[:date] = Time::parse(result[:created_time]).strftime("%Y-%m-%d %H:%M:%S")
					response << topic
				end
			end
		rescue Koala::KoalaError
			halt 400
		end
		
		content_type :json
		response.to_json
	end

	get '/search/facebook' do
		halt 400 if params[:access_token].blank?

		queries = Array.new
		if !params[:first_name_en].blank? && !params[:last_name_en].blank? then
			queries << "#{params[:first_name_en]} #{params[:last_name_en]}"
		elsif !params[:first_name_kana].blank? && !params[:last_name_kana].blank? then
			queries << "#{params[:first_name_kana].to_roman} #{params[:last_name_kana].to_roman}"
			queries << "#{params[:first_name_kana].to_roman.to_kunrei} #{params[:last_name_kana].to_roman.to_kunrei}"
		else
			halt 400
		end

		ids = Array.new
		response = Array.new
		graph = Koala::Facebook::API.new(params[:access_token])
		begin
			# user search 
			queries.each do |query|
				graph.graph_call('search', :type => 'user', :q => query).each do |result|
					result = result.symbolize_keys
					ids << result[:id]
				end
			end

			# get user information
			graph.get_objects(ids.uniq, :locale => 'ja_JP').each do |result|
				result = result.last.symbolize_keys

				next if (!params[:first_name_ja].blank?) && (params[:first_name_ja] != result[:first_name])
				next if (!params[:last_name_ja].blank?) && (params[:last_name_ja] != result[:last_name])

				response << {
					:id => result[:id],
					:name => result[:name],
					:gender => result[:gender],
					:locale => result[:locale],
					:link => result[:link],
					:picture => graph.get_picture(result[:id], :type => 'large')}
			end
		rescue Koala::KoalaError
			halt 400
		end

		content_type :json
		response.to_json
	end

	get '/keyphrase/facebook' do
		halt 400 if params[:access_token].blank?

		# create queries
		queries = Array.new
		graph = Koala::Facebook::API.new(params[:access_token])
		begin
			# get posts
			graph.get_connections('me', 'feed', :limit => 100).each do |result|
				result = result.symbolize_keys

				# message
				queries << result[:message] unless result[:message].nil?
				# shared link
				if result[:type] == 'link' and result[:status_type] == 'shared_story' then
					queries << result[:name] unless result[:name].nil?
					queries << result[:description] unless result[:description].nil?
				end
			end
		rescue Koala::KoalaError
			halt 400
		end
		
		# get keyphrases
		response = Array.new
		queries.each do |query|
			next unless query =~ /[^ -~｡-ﾟ]/
			Net::HTTP.start(settings.yahoo_api[:host]) do |http|
				res = http.post(settings.yahoo_api[:path], "appid=#{settings.yahoo_api[:app_id]}&sentence=#{URI.encode(query)}")
				REXML::Document.new(res.body).elements.each('ResultSet/Result') do |result|
					response << result.elements['Keyphrase'].text if result.elements['Score'].text.to_i == 100
				end
			end
		end

		content_type :json
		response.to_json
	end

	get '/auth/facebook' do
		redirect oauth_facebook.url_for_oauth_code(:scope => 'read_stream')
	end

	get '/auth/facebook/callback' do
		halt 400 if params[:code].blank?
		"<script>Login.sendAccessToken(\"#{oauth_facebook.get_access_token(params[:code])}\");</script>"
	end
end
