#!/usr/local/bin/ruby
require "webrick"

port = ARGV[1] || 8888
server = WEBrick::HTTPServer.new({:DocumentRoot => ".",
                                  :BindAddress => "127.0.0.1",
                                  :Port => port})
trap('INT'){ server.shutdown }
server.start
