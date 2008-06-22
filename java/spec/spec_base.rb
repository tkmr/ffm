module FFM
  include_package "ffm"
  module FFM::JS
    include_package "ffm.js"
  end
  module FFM::Socket
    include_package "ffm.socket"
  end
  module FFM::UPnP
    include_package "ffm.upnp"
  end
end
