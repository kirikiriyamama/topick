require 'bundler'
Bundler.setup
Bundler.require


DIR = File.expand_path(File.dirname(__FILE__))
FACEBOOK_CONF = File.join(DIR, 'config', 'facebook.yml')

require File.join(DIR, 'app')
run Topick
