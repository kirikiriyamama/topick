def oauth_twitter
	OAuth::Consumer.new(
		settings.twitter[:consumer_key],
		settings.twitter[:consumer_secret],
		:site => 'https://api.twitter.com')
end
