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
		enable :sessions

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
		halt 400 if session[:access_token_facebook].blank?
		halt 400 if params[:id].blank?
		halt 400 if params[:keyphrase].blank?

		keyphrase = String.new
		params[:keyphrase].split(',').each do |str|
			keyphrase << "(#{str})|"
		end
		keyphrase.chop!
		
		topic = Array.new
		graph = Koala::Facebook::API.new(session[:access_token_facebook])
		begin
			# if text =~ Regexp.new(keyphrase) then
			graph.get_connections(params[:id], 'posts', :limit => 50).each do |feed|
				if !feed['message'].nil? && feed['message'] =~ Regexp.new(keyphrase) then
					topic << feed
					next
				end
				if feed['type'] == 'link' and feed['status_type'] == 'shared_story' then
					if !feed['name'].nil? && feed['name'] =~ Regexp.new(keyphrase) then
						topic << feed
						next
					end
					if !feed['description'].nil? && feed['description'] =~ Regexp.new(keyphrase) then
						topic << feed
						next
					end
				end
			end
			pp topic
		rescue
			halt 400
		end

		200
	end

	get '/search/facebook' do
		halt 400 if session[:access_token_facebook].blank?

		q = Array.new
		if !params[:first_name_en].blank? && !params[:last_name_en].blank? then
			q << "#{params[:first_name_en]} #{params[:last_name_en]}"
		elsif !params[:first_name_kana].blank? && !params[:last_name_kana].blank? then
			q << "#{params[:first_name_kana].to_roman} #{params[:last_name_kana].to_roman}"
			q << "#{params[:first_name_kana].to_roman.to_kunrei} #{params[:last_name_kana].to_roman.to_kunrei}"
		else
			halt 400
		end

		users = Array.new
		results = Array.new
		graph = Koala::Facebook::API.new(session[:access_token_facebook])
		begin
			q.each do |name|
				graph.graph_call('search', :type => 'user', :q => name).each do |user|
					users << user['id']
				end
			end

			graph.get_objects(users.uniq, :locale => 'ja_JP').each do |user_info|
				next if (!params[:first_name_ja].blank?) && (params[:first_name_ja] != user_info.last['first_name'])
				next if (!params[:last_name_ja].blank?) && (params[:last_name_ja] != user_info.last['last_name'])
				user_info.last['picture'] = graph.get_picture(user_info.last['id'], :type => 'large')
				results << user_info.last
			end
			pp results
		rescue Koala::KoalaError
			halt 400
		end

		200
	end

	get '/keyphrase/facebook' do
		halt 400 if session[:access_token_facebook].blank?

		posts = Array.new
		graph = Koala::Facebook::API.new(session[:access_token_facebook])
		begin
			graph.get_connections('me', 'feed', :limit => 50).each do |feed|
				posts << feed['message'] unless feed['message'].nil?
				if feed['type'] == 'link' and feed['status_type'] == 'shared_story' then
					posts << feed['name'] unless feed['name'].nil?
					posts << feed['description'] unless feed['description'].nil?
				end
			end
		rescue Koala::KoalaError
			halt 400
		end
		
		posts.each do |post|
			next unless post =~ /[^ -~｡-ﾟ]/
			Net::HTTP.start(settings.yahoo_api[:host]) do |http|
				response = http.post(settings.yahoo_api[:path], "appid=#{settings.yahoo_api[:app_id]}&sentence=#{URI.encode(post)}")
				REXML::Document.new(response.body).elements.each('ResultSet/Result') do |result|
					puts "#{result.elements['Score'].text.rjust(3, '0')}: #{result.elements['Keyphrase'].text}"
				end
			end
		end

		200
	end

	get '/auth/facebook' do
		halt 400 if session[:access_token_facebook].blank?
		"<script>Login.sendAccessToken(\"#{session[:access_token_facebook]}\");</script>"
	end

	get '/auth/facebook/login' do
		session[:access_token_facebook] = nil
		redirect oauth_facebook.url_for_oauth_code(:scope => 'read_stream')
	end

	get '/auth/facebook/callback' do
		halt 400 if params[:code].blank?
		session[:access_token_facebook] = oauth_facebook.get_access_token(params[:code])
		# redirect '/search/facebook?first_name_en=youiti&last_name_en=tanabe&first_name_ja=%E6%B4%8B%E4%B8%80&last_name_ja=%E7%94%B0%E8%BE%BA'
		redirect '/topic/facebook?id=100001546266000'
	end
end
