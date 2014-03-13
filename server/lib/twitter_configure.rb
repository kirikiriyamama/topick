# -*- coding: utf-8 -*-

def twitter_configure(access_token, access_token_secret)
  Twitter.configure do |config|
    config.consumer_key = settings.twitter[:consumer_key]
    config.consumer_secret = settings.twitter[:consumer_secret]
    config.oauth_token = access_token
    config.oauth_token_secret = access_token_secret
  end
  Twitter::Client.new
end
