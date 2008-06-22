require 'java'
require 'lib/jssocket.jar'

module FFM
  include_package "ffm"
  include_package "ffm.js"
  include_package "ffm.socket"
  include_package "ffm.upnp"
end

describe FFM::JSocketApplet, "Spec of JSocketApplet" do
  before do
    @applet = FFM::JSocketApplet.new()
  end

  it "echo method return a value which a input" do
    @applet.echo("Hello world").should eql "Hello world"
  end

#  it "createTCPSocket method return a ffm.socket.TCPSocket class instance" do
#    socket = @applet.createTCPSocket(8888)
#    socket.getClass().getName().should eql "ffm.socket.TCPSocket"
#  end

end
