class Topick < Sinatra::Base
	configure :development do
		Bundler.require :development
		register Sinatra::Reloader

		require 'pp'
	end

	configure do
		set :sessions, true
		enable :sessions

		set(:facebook) { YAML.load_file(FACEBOOK_CONF).symbolize_keys }
	end
	
	def initialize
		super
		@oauth = Koala::Facebook::OAuth.new(settings.facebook[:app_id], settings.facebook[:app_secret], 'http://api.sansan-plus-plus.tk/callback')
	end


	get '/' do
		if session[:access_token] then
			graph = Koala::Facebook::API.new(session[:access_token])
			graph.get_connections('me', 'posts', :limit => 50).each do |post|
				p post['message'] unless post['message'].nil?
				if post['type'] == 'link' and post['status_type'] == 'shared_story' then
					p post['name'] unless post['name'].nil?
					p post['description'] unless post['description'].nil?
				end
			end
			
			session[:access_token]
		else
			redirect '/login'
		end
	end

	get '/login' do
		session[:access_token] = nil
		redirect @oauth.url_for_oauth_code(:scope => 'read_stream')
	end

	get '/logout' do
		session[:access_token] = nil
		redirect '/'
	end

	get '/callback' do
		if params[:code] then
			session[:access_token] =  @oauth.get_access_token(params[:code])
			redirect '/'
		end
	end
end
