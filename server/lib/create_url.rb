def create_url(scheme, host, path, parameter = {})
	url = "#{scheme}://#{host}#{path}"
	unless parameter.blank? then
		url << '?'
		parameter.each do |key, value|
			url << "#{key}=#{value}&"
		end
		url.chop!
	end
	url
end
