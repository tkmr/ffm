var ffm = ffm||{};

(function(ffm){
  //////////////////////////////////////////////////////////////////////
  /*
   * Configulation
   */
  ffm.Config = {
    defaultTimeout: 5000
  }

  ffm.Temp = {
    ID: {},
    Callback: {}
  };

  ffm.Util = {
    debug: function(info){
      if(window["console"] !== undefined){
        console.log(info);
      }
    },
    extend: function(src, dest){
      for(var key in src.prototype){
        dest.prototype[key] = src.prototype[key];
      }
    },
    CRLFtoF8F8: function(req){
      return req.replace(/\n/g, "\uf8f8");
    },
    F8F8toCRLF: function(req){
      return req.replace(/\uf8f8/g, "\n");
    },
    uniqueID: function(){
      var id = Math.floor(Math.random() * 10000000).toString();
      while(ffm.Temp.ID[id] !== undefined){
        id = Math.floor(Math.random() * 10000000).toString();
      }
      ffm.Temp.ID[id] = true;
      return id;
    }
  }

  ///////////////////////////////////////////////////////////////////////
  /*
   * Socket class
   */
  ffm.BaseSocket = function(applet){
    this.init(applet);
  }

  ffm.BaseSocket.prototype.init = function(applet){
    if(applet === null || typeof(applet) === "undefined"){
      this.applet = document.applet;
      //TODO load applet use DOM;
    }else{
      this.applet = applet;
    }
  }

  ffm.BaseSocket.prototype.connect = function(host, timeout, callback){
    timeout = timeout || ffm.Config.defaultTimeout;
    if(typeof(callback) === "function"){
      this.socket.connect(host, timeout);
      callback(this);
      this.close();
    }else{
      this.socket.connect(host, timeout);
    }
  }

  ffm.BaseSocket.prototype.write = function(request){
    if(typeof(request) === "object"){
      var req = java.lang.reflect.Array.newInstance(java.lang.String, request.length);
      for(var i=0; i < request.length; i++){
        req[i] = request[i];
      }
      request = req;
    }
    this.socket.write(request);
  }

  ffm.BaseSocket.prototype.readAll = function(){
    return this.socket.readAll();
  }

  ffm.BaseSocket.prototype.readLine = function(){
    return this.socket.readLine();
  }

  ffm.BaseSocket.prototype.read = function(length){
    return (length === undefined ? String.fromCharCode(this.socket.read()) : this.socket.read(length));
  }

  ffm.BaseSocket.prototype.readyRead = function(){
    return this.socket.readyRead();
  }

  ffm.BaseSocket.prototype.close = function(){
    return this.socket.close();
  }

  ffm.BaseSocket.prototype.isClosed = function(){
    return this.socket.isClosed();
  }

  ffm.BaseSocket.prototype.getPort = function(){
    return this.socket.getPort();
  }

  ///////////////////////////////////////////////////////////////////////
  /*
   * TCpSocket class
   */
  ffm.TCPSocket = function(port, applet, javaSocket){
    this.init(applet);
    this.port = port;
    this.socket = (javaSocket === undefined) ? this.applet.createTCPSocket(port) : javaSocket;
  }
  ffm.Util.extend(ffm.BaseSocket, ffm.TCPSocket);

})(ffm);
