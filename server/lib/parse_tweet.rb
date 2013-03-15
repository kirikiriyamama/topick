# -*- coding: utf-8 -*-


def parse_tweet(tweet)
	# when could not get tweets
	if !tweet.instance_of?(Twitter::Tweet) then
		raise 'ArgumentError: invalid argument'
	end
	
	text = tweet.text
	urls = tweet.urls
	source = tweet.source
	

	# TweetButton, twittbot.net, このまま眠りつづけて死ぬ, gohantabeyo.com, Hatena, 夏休みはあとn日しかない, 劣化コピー, Tumblr
	if source.gsub!(/<("[^"]*"|'[^']*'|[^'">])*>/, '') =~ /Tweet\sButton|twittbot.net|このまま眠りつづけて死ぬ|gohantabeyo.com|Hatena|夏休みはあとn日しかない|劣化コピー|Tumblr/
		return nil
	end
	
	# shindanmaker
	urls.each do |url|
		if url.expanded_url =~ /shindanmaker/ then
			return nil
		end
	end

	# foursquare, shootingstar, fav2you
	if text =~ /(^I'm at )|((?<!\d)\d{1,3}EXP(?!\w))|(#fav2you)/ then
		return nil
	end

	# foursquare
	if n = (text =~ /\s\(@\s.*\)/) then
		text = text.slice(0, n)
	end

	# path (pic)
	if n = (text =~ /\s(\(at\s.*\)\s)?\[pic\]\s—/) then
		text = text.slice(0, n)
	end

	# ust
	if n = (text =~ /\s\(.*live\sat.*\)/) then
		text = text.slice(0, n)
	end
	
	# unofficial retweet
	if n = (text =~ /\s?(RT|QT)/) then
		text = text.slice(0, n)
	end

	# reply
	text.gsub!(/(\.?|(?<![\w&_\/]))@([\w_]{1,15})/, '')

	# hashtag
	text.gsub!(/(?<![ｦ-ﾟー゛゜々ヾヽぁ-ヶ一-龠ａ-ｚＡ-Ｚ０-９\w&_\/])[#＃]([ｦ-ﾟー゛゜々ヾヽぁ-ヶ>一-龠ａ-ｚＡ-Ｚ０-９\w_]*[ｦ-ﾟー゛゜々ヾヽぁ-ヶ一-龠ａ-ｚＡ-Ｚ０-９a-zA-Z]+[ｦ-ﾟー゛゜々ヾヽぁ-ヶ一-龠>ａ-ｚＡ-Ｚ０-９\w_]*)/, '')

	# URL
	text.gsub!(/(https?|ftp):\/\/[\w_,.:;&=+*%$#!?@()~\'\/-]+/, '')
	
	# space at the beginning and end of a sentence
	text.gsub!(Regexp.new("^[\s　]+|[\s|　]+$"), '')
	

	if text.size.zero? then
		return nil
	else
		return text
	end
end
