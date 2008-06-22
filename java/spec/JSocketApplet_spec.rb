require 'java'
require 'lib/jssocket.jar'
require 'spec/spec_base'

describe FFM::JSocketApplet, "Spec of JSocketApplet" do
  before do
    @applet = FFM::JSocketApplet.new()
  end

  it "echo method return a value which a input" do
    @applet.echo("Hello world").should eql "Hello world"
  end

  it "createTCPSocket method return a ffm.socket.TCPSocket class instance" do
    socket = @applet.createTCPSocket(8888)
    socket.getClass().getName().should eql "ffm.socket.TCPSocket"
    socket.class.should eql FFM::Socket::TCPSocket
    socket.port.should eql 8888
  end

  it "createSocketRequest method return a ffm.socket.SocketRequest class instance" do
    socket = @applet.createTCPSocket(8888)
    request = @applet.createSocketRequest(socket)

    request.class.should eql FFM::Socket::SocketRequest
    request.socket.class.should eql FFM::Socket::TCPSocket
    request.socket.port.should eql 8888
  end
end
