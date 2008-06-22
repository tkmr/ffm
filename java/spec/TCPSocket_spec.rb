require 'java'
require 'lib/jssocket.jar'
require 'spec/spec_base'

describe FFM::Socket::TCPSocket do
  before do
    @socket = FFM::Socket::TCPSocket.new(80)
  end

  it "can get a web page used http over tcp" do
    @socket.connect("www.google.com", 5000) #time out 5 seconds.
    @socket.write("GET / HTTP/1.1\r\nHost: www.google.com\r\n\r\n")
    result = @socket.read();
    result.should match /<html.*<\/html>/
  end
end
