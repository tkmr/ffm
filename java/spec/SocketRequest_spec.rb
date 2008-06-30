require 'java'
require 'lib/jssocket.jar'
require 'spec/spec_base'

describe FFM::Socket::SocketRequest do
  before do
  end

  it "can get a web page used http over tcp, readAll" do
    connect_google do |socket|
      result = socket.readAll();
      result.gsub(/\n/,"").should match /<html.*<\/html>/i
    end
  end
end
