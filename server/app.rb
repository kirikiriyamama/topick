require 'open-uri'
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
		set :sessions, true
		enable :sessions

		set(:facebook) { YAML.load_file(FACEBOOK_CONF).symbolize_keys }
		set(:yahoo_api) { YAML.load_file(YAHOO_API_CONF).symbolize_keys }
	end
	
	def initialize
		super
		@oauth = Koala::Facebook::OAuth.new(settings.facebook[:app_id], settings.facebook[:app_secret], 'http://api.sansan-plus-plus.tk/callback')
	end


	get '/' do
		if session[:access_token] then
			posts = Array.new
			graph = Koala::Facebook::API.new(session[:access_token])
			graph.get_connections('me', 'posts', :limit => 50).each do |feed|
				posts << feed['message'] unless feed['message'].nil?
				if feed['type'] == 'link' and feed['status_type'] == 'shared_story' then
					posts << feed['name'] unless feed['name'].nil?
					posts << feed['description'] unless feed['description'].nil?
				end
			end
			
			posts.each do |post|
				parameter = {
					:appid => settings.yahoo_api[:app_id],
					:sentence => URI.encode(post)
				}
				url = create_url('http', settings.yahoo_api[:host], settings.yahoo_api[:path], parameter)

				REXML::Document.new(open(url)).elements.each('ResultSet/Result') do |result|
					puts "#{result.elements['Score'].text.rjust(3, '0')}: #{result.elements['Keyphrase'].text}"
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
