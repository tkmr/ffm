require 'java'
require 'lib/jssocket.jar'
require 'spec/spec_base'

describe FFM::Socket::TCPSocket do
  before do
  end

  it "can get a web page used http over tcp, readAll" do
    connect_google do |socket|
      result = socket.readAll();
      result.gsub(/\n/,"").should match /<html.*<\/html>/i
    end
  end

  it "can get a web page used http over tcp, readLine" do
    connect_google do |socket|
      results = []
      while((result = socket.readLine()) != nil)
        results << result
      end
      results.size.should > 1
      results.join("").should match /<html.*<\/html>/i
    end
  end

  it "can get a web page used http over tcp, read one charcter" do
    connect_google do |socket|
      #HTTP/1.1 200 OK
      socket.read().chr.should eql "H"
      socket.read().chr.should eql "T"
      socket.read().chr.should eql "T"
      socket.read().chr.should eql "P"
      socket.read().chr.should eql "/"
    end
  end

  it "can get a web page used http over tcp, read multi charcter" do
    connect_google do |socket|
      #HTTP/1.1 200 OK
      socket.read(8).should eql "HTTP/1.1"
      socket.read(7).should eql " 200 OK"
    end
  end

  it "should close socket when call close method" do
    socket = FFM::Socket::TCPSocket.new(80)
    socket.connect("www.google.com", 5000)
    socket.close
    socket.isClosed.should eql true

    socket = FFM::Socket::TCPSocket.new(80)
    socket.close
    socket.isClosed.should eql true
  end
end

def connect_google
  socket = FFM::Socket::TCPSocket.new(80)
  socket.connect("www.google.com", 5000)
  socket.write("GET /webhp HTTP/1.1\r\nHost: www.google.com\r\nConnection: close\r\n\r\n")
  yield(socket)
  socket.close
end
