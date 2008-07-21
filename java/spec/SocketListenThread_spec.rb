require 'java'
require 'lib/jssocket.jar'
require 'spec/spec_base'

describe FFM::Socket::SocketListenThread do
  before do
    @port = 20000 ##+ (rand * 1000).to_i
    @socket = FFM::Socket::TCPSocket.new(@port)
    @listenCallback = FFM::Mock::MockCallback.new("echoToSocket")
  end

  it "can generate a sub thread when accepted new socket request" do
    server = FFM::Socket::SocketListenThread.new(@socket, @listenCallback)
    server.start
    connect_server do |socket|
      socket.write("hello world\n")
      socket.readLine().should eql "request is hello world"
    end
    connect_server do |socket|
      socket.write("hello byte\n")
      socket.read(21).should eql "request is hello byte"
    end
    server.close
    server.join
    server.isClosed.should eql true
  end

  it "close a socket when a @threadCallback return null" do
    server = FFM::Socket::SocketListenThread.new(@socket, @listenCallback)
    server.start
    connect_server {|socket| socket.write("test") }
    connect_server {|socket| socket.write("test") }
    server.close
    server.join

    server.isClosed.should eql true
    lambda{ connect_server {|socket| }}.should raise_error(java.net.ConnectException)
  end

  it "close a socketServer and close some sub threads" do
    server = FFM::Socket::SocketListenThread.new(@socket, @listenCallback)
    server.start
    connect_server do |socket|
      socket.write("hello socket\n")
      socket.readLine().should eql "request is hello socket"
    end
    server.close
    server.join

    server.isClosed.should eql true
    server.getSubThreads.each do |subThread|
      subThread.isClosed.should eql true
    end
  end

  it "wait some time in SocketListenSubThread" do
    false.should eql true
  end

  def connect_server
    socket = FFM::Socket::TCPSocket.new(@port)
    socket.connect("localhost")
    yield(socket)
    socket.close
    socket.isClosed.should eql true
  end
end
