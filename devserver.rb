#!/usr/local/bin/ruby
require "webrick"

port = ARGV[1] || 8888
server = WEBrick::HTTPServer.new({:DocumentRoot => ".",
                                  :BindAddress => "0.0.0.0",
                                  :Port => port})
trap('INT'){ server.shutdown }
server.start
