require File.join(DIR, 'lib', 'Hash')
require File.join(DIR, 'lib', 'create_url')


class Topick < Sinatra::Base
	configure :development do
		Bundler.require :development
		register Sinatra::Reloader
	end

	configure do
		enable :sessions

		set(:facebook) { YAML.load_file(FACEBOOK_CONF).symbolize_keys }
	end
	
	def oauth_facebook
		Koala::Facebook::OAuth.new(
			settings.facebook[:app_id],
			settings.facebook[:app_secret],
			create_url(request.scheme, request.host, '/auth/facebook/callback'))
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
		redirect '/auth/facebook'
	end
end
