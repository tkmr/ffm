require 'java'
require 'lib/jssocket.jar'
require 'spec/spec_base'

describe FFM::Socket::SocketListenThread do
  before do
    @post = 8585
    @socket = FFM::Socket::SocketListenThread.new(@port)
    ##map = java.util.HashMap.new()
    @threadCallback = FFM::Mock::MockCallback.new({"1"=>"ok", "2"=>"ok", "3"=>"ok"})
  end

  it "can generate a sub thread when accepted new socket request" do
		listenCallback = FFM::Mock::MockCallback.new({"1"=>"ok", "2"=>"ok", "3"=>"ok"})
    thread = FFM::Socket::SocketListenThread.new(@port, @socket, listenCallback, @threadCallback)
		thread.start()

		
  end
end
