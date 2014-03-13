# -*- coding: utf-8 -*-

require 'net/http'
require 'rexml/document'

def get_keyphrases(queries)
  keyphrases = Array.new
  queries.each do |query|
    next unless query =~ /[^ -~｡-ﾟ]/
      Net::HTTP.start(settings.yahoo_api[:host]) do |http|
      response = http.post(settings.yahoo_api[:path], "appid=#{settings.yahoo_api[:app_id]}&sentence=#{URI.encode(query)}")
      REXML::Document.new(response.body).elements.each('ResultSet/Result') do |result|
        keyphrases << result.elements['Keyphrase'].text if result.elements['Score'].text.to_i == 100
      end
      end
  end
  keyphrases.uniq
end
