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
		session[:access_token]
	end

	get '/login' do
		session[:access_token] = nil
		redirect @oauth.url_for_oauth_code
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
