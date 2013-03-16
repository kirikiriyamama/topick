def to_regexp(words)
	string = String.new
	words.each do |word|
		string << "(#{word})|"
	end
	Regexp.new(string.chop)
end
