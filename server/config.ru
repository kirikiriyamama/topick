require 'bundler'
Bundler.setup
Bundler.require

DIR = File.expand_path(File.dirname(__FILE__))
require File.join(DIR, 'app')

run Topick
