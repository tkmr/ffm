require 'java'
require 'lib/jssocket.jar'
require 'spec/spec_base'

describe FFM::JSocketApplet do
  before do
    @applet = FFM::JSocketApplet.new()
  end

  it "has createTCPSocket method, it return a ffm.socket.TCPSocket class instance" do
    socket = @applet.createTCPSocket(8888)
    socket.getClass().getName().should eql "ffm.socket.TCPSocket"
    socket.class.should eql FFM::Socket::TCPSocket
    socket.port.should eql 8888
  end

  it "has createSocketListenThread method, it return a ffm.socket.SocketListenThread class instance" do
    socket = @applet.createTCPSocket(8888)
    thread = @applet.createSocketListenThread(socket, "listenCallback")
    thread.getClass().getName().should eql "ffm.socket.SocketListenThread"
  end

  it "can close all thread" do
    thread1 = @applet.createSocketListenThread(@applet.createTCPSocket(8891), "listenCallback")
    thread2 = @applet.createSocketListenThread(@applet.createTCPSocket(8892), "listenCallback")
    thread3 = @applet.createSocketListenThread(@applet.createTCPSocket(8893), "listenCallback")
    @applet.close
    thread1.isClosed.should eql true
    thread2.isClosed.should eql true
    thread3.isClosed.should eql true
  end
end
