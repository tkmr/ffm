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
    thread = @applet.createSocketListenThread(socket, "listenCallback", "threadCallback")
    thread.getClass().getName().should eql "ffm.socket.SocketListenThread"
  end
end
