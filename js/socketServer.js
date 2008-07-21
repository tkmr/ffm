var ffm = ffm||{};
(function(ffm){
  ///////////////////////////////////////////////////////////////////////
  /*
   * SocketServer class
   */
  ffm.SocketServer = function(socket, callback){
    this.socket = socket;
    var self = this;

    this.callback = function(javaSocket){
      var jsSocket = new ffm.TCPSocket(javaSocket.getPort(), self.socket.applet, javaSocket);
      jsSocket.socket = javaSocket;
      callback(jsSocket);
    };

    var id = ffm.Util.uniqueID();
    window["ffm_Temp_Callback_"+id] = this.callback;
    this.server = socket.applet.createSocketListenThread(this.socket.socket, "ffm_Temp_Callback_"+id);
  }

  ffm.SocketServer.prototype.start = function(){
    this.server.start();
  }
  ffm.SocketServer.prototype.close = function(){
    this.server.close();
  }
  ffm.SocketServer.prototype.join = function(){
    this.server.join();
  }
  ffm.SocketServer.prototype.isClosed = function(){
    return this.server.isClosed();
  }

})(ffm);
