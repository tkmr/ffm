require 'java'
require 'lib/jssocket.jar'
require 'spec/spec_base'

describe FFM::Socket::TCPSocket do
  before do
    @socket = FFM::Socket::TCPSocket.new(80)
  end

  it "can get a web page, and it have readAll() method." do
    @socket.connect("www.google.com", 5000) #time out 5 seconds.
    @socket.write("GET / HTTP/1.1\r\nHost: www.google.com\r\nConnection: close\r\n\r\n")
    result = @socket.readAll()
    @socket.close()
    result.should match /<html.*<\/html>/i
  end

  it "have read() method and get one byte" do
    @socket.connect("www.google.com", 5000) #time out 5 seconds.
    @socket.write("GET / HTTP/1.1\r\nHost: www.google.com\r\nConnection: close\r\n\r\n")
    result = ""
    while((tmp_res = @socket.read()) != -1)
      result += tmp_res.to_s
    end
    @socket.close()
    result.size.should >= 100
  end

  it "have read() method and get some bytes" do
    @socket.connect("www.google.com", 5000) #time out 5 seconds.
    @socket.write("GET / HTTP/1.1\r\nHost: www.google.com\r\nConnection: close\r\n\r\n")
    result = ""
    while((tmp_res = @socket.read()) != -1)
      result += tmp_res.to_s
    end
    @socket.close()
    result.size > 100
  end
end
