def search_keyphrase(text, keyphrase)
	return nil if text.nil?

	result = Array.new
	keyphrase.split(/\s*,\s*/).each do |str|
		result << str if text =~ Regexp.new(str, Regexp::IGNORECASE)
	end
	result
end
