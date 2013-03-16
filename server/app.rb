require File.join(DIR, 'lib', 'Hash')
require File.join(DIR, 'lib', 'create_url')
require File.join(DIR, 'lib', 'oauth_facebook')
require File.join(DIR, 'lib', 'oauth_twitter')
require File.join(DIR, 'lib', 'twitter_configure')
require File.join(DIR, 'lib', 'parse_tweet')
require File.join(DIR, 'lib', 'get_keyphrases')
require File.join(DIR, 'lib', 'to_regexp')


class Topick < Sinatra::Base
	configure :development do
		Bundler.require :development
		register Sinatra::Reloader

		require 'pp'
	end

	configure do
		set :sessions, true
		enable :sessions

		set(:facebook) { YAML.load_file(File.join(DIR, 'config', 'facebook.yml')).symbolize_keys }
		set(:twitter) { YAML.load_file(File.join(DIR, 'config', 'twitter.yml')).symbolize_keys }
		set(:yahoo_api) { YAML.load_file(File.join(DIR, 'config', 'yahoo_api.yml')).symbolize_keys }
	end


	get '/topic/facebook' do
		halt 400 if params[:access_token].blank?
		halt 400 if params[:target_user].blank?
		halt 400 if params[:keyphrase].blank?

		keyphrase = to_regexp(params[:keyphrase].split(','))

		response = Array.new
		graph = Koala::Facebook::API.new(params[:access_token])
		begin
			graph.get_connections(params[:target_user], 'posts', :limit => 100).each do |result|
				result = result.symbolize_keys
				flg = false

				topic = Hash.new
				topic[:summary] = nil
				topic[:shared_link] = { :summary => nil, :link => nil }

				# message
				if !result[:message].nil? && (result[:message] =~ keyphrase) then
					flg = true
					topic[:summary] = result[:message].slice(0, 20)
				end

				# shared link
				if (result[:type] == 'link') && (result[:status_type] == 'shared_story') then
					if ((!result[:name].nil? && (result[:name] =~ keyphrase)) || (!result[:description].nil? && (result[:description] =~ keyphrase))) || flg then
						flg = true
						topic[:shared_link][:summary] = result[:name].slice(0, 20) unless result[:name].nil?
						topic[:shared_link][:link] = result[:link]
					end
				end
				
				if flg then
					id = result[:id].split('_')
					topic[:link] = create_url('http', Koala::Facebook::DIALOG_HOST, "/#{id.first}/posts/#{id.last}")
					topic[:date] = Time::parse(result[:created_time]).strftime("%Y-%m-%d %H:%M:%S")
					response << topic
				end
			end
		rescue Koala::Facebook::ClientError
			halt 400
		end

		pp response

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
		rescue Koala::Facebook::ClientError
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
		rescue Koala::Facebook::ClientError
			halt 400
		end
		
		content_type :json
		get_keyphrases(queries).to_json
	end

	get '/auth/facebook' do
		redirect oauth_facebook.url_for_oauth_code(:scope => 'read_stream')
	end

	get '/auth/facebook/callback' do
		halt 400 if params[:code].blank?
		"<script>Login.sendFacebookAccessToken(\"#{oauth_facebook.get_access_token(params[:code])}\");</script>"
	end


	get '/topic/twitter' do
		halt 400 if params[:access_token].blank?
		halt 400 if params[:access_token_secret].blank?
		halt 400 if params[:target_user].blank?
		halt 400 if params[:keyphrase].blank?

		keyphrase = to_regexp(params[:keyphrase].split(','))

		response = Array.new
		twitter = twitter_configure(params[:access_token], params[:access_token_secret])
		begin
			twitter.user_timeline(:count => 100).each do |result|
				topic = Hash.new

				text = parse_tweet(result)
				if !text.nil? && (text =~ keyphrase) then
					topic[:summary] = result.text.slice(0, 20)
					topic[:shared_links] = Array.new
					unless result.urls.blank? then
						result.urls.each do |url|
							topic[:shared_links] << url.expanded_url
						end
					end
					topic[:link] = create_url('https', 'twitter.com', "/#{result.user.screen_name}/status/#{result.id}")
					topic[:date] = result.created_at.strftime("%Y-%m-%d %H:%M:%S")

					response << topic
				end
			end
		rescue Twitter::Error::ClientError
			halt 400
		end

		content_type :json
		response.to_json
	end

	get '/search/twitter' do
		halt 400 if params[:access_token].blank?
		halt 400 if params[:access_token_secret].blank?
		halt 400 if params[:screen_name].blank?

		response = Array.new
		twitter = twitter_configure(params[:access_token], params[:access_token_secret])
		begin
			unless (result = twitter.user_search(params[:screen_name]).first).blank? then
				pp result
				response << {
					:id => result.id,
					:screen_name => result.screen_name,
					:name => result.name,
					:description => result.description,
					:link => create_url('https', 'twitter.com', "/#{result.screen_name}"),
					:picture => result.profile_image_url(:bigger)}
			end
		rescue Twitter::Error::ClientError
			halt 400
		end

		content_type :json
		response.to_json
	end

	get '/keyphrase/twitter' do
		halt 400 if params[:access_token].blank?
		halt 400 if params[:access_token_secret].blank?
		
		queries = Array.new
		twitter = twitter_configure(params[:access_token], params[:access_token_secret])
		begin
			twitter.user_timeline(:count => 100).each do |result|
				text = parse_tweet(result)
				queries << text unless text.nil?
			end
		rescue Twitter::Error::ClientError
			halt 400
		end

		content_type :json
		get_keyphrases(queries).to_json
	end

	get '/auth/twitter' do
		request_token = oauth_twitter.get_request_token(:oauth_callback => create_url(request.scheme, request.host, '/auth/twitter/callback'))
		session[:request_token] = request_token.token
		session[:request_token_secret] = request_token.secret
		redirect request_token.authorize_url
	end

	get '/auth/twitter/callback' do
		begin
			request_token = OAuth::RequestToken.new(oauth_twitter, session[:request_token], session[:request_token_secret])
			access_token = request_token.get_access_token(:oauth_token => params[:oauth_token], :oauth_verifier => params[:oauth_verifier])
		rescue Oauth::Unauthorized
			halt 400
		end
		pp "<script>Login.sendTwitterAccessToken(\"#{access_token.token}\", \"#{access_token.secret}\");</script>"
	end
end
