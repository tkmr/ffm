require 'java'
require 'lib/jssocket.jar'
require 'spec/spec_base'

describe FFM::Socket::SocketListenThread do
  before do
    @port = 20000 + (rand * 1000).to_i
    socket = FFM::Socket::TCPSocket.new(@port)
    @threadCallback = FFM::Mock::MockCallback.new("echoNabeatsu")
    @listenCallback = FFM::Mock::MockCallback.new("echoToSocket")
    FFM::Socket::SocketListenThread.new(socket, @listenCallback, @threadCallback).start()
  end

  it "can generate a sub thread when accepted new socket request" do
    connect_server do |socket|
      socket.write("hello world\n")
      socket.readLine().should eql "request is hello world"
    end
    connect_server do |socket|
      socket.write("hello byte\n")
      socket.read(21).should eql "request is hello byte"
    end
  end

  it "close a socket when a @threadCallback return null" do
    connect_server {|socket| } #0
    connect_server {|socket| } #1
    connect_server {|socket| } #2
    lambda{ connect_server {|socket| }}.should raise_error(java.net.ConnectException) #Sann
  end

  def connect_server
    socket = FFM::Socket::TCPSocket.new(@port)
    socket.connect("localhost")
    yield(socket)
    socket.close
    socket.isClosed.should eql true
  end
end
