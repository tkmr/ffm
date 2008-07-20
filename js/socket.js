var ffm = ffm||{};

(function(ffm){
  //////////////////////////////////////////////////////////////////////
  /*
   * Configulation
   */
  ffm.Config = {
    defaultTimeout: 5000
  }

  ffm.Temp = {};

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
    this.socket.write(request);
  }

  ffm.BaseSocket.prototype.readAll = function(){
    return this.socket.readAll();
    //return ffm.Util.F8F8toCRLF(result);
  }

  ffm.BaseSocket.prototype.readLine = function(){
    return this.socket.readLine();
    //return ffm.Util.F8F8toCRLF(result);
  }

  ffm.BaseSocket.prototype.read = function(length){
    return (length === undefined ? this.socket.read() : this.socket.read(length));
    //return ffm.Util.F8F8toCRLF(result);
  }

  ffm.BaseSocket.prototype.close = function(){
    this.socket.close();
  }

  ffm.BaseSocket.prototype.isClosed = function(){
    this.socket.isClosed();
  }

  ffm.BaseSocket.prototype.getPort = function(){
    this.socket.getPort();
  }

  ///////////////////////////////////////////////////////////////////////
  /*
   * TCpSocket class
   */
  ffm.TCPSocket = function(port, applet){
    this.init(applet);
    this.port = port;
    this.socket = this.applet.createTCPSocket(port);
  }
  ffm.Util.extend(ffm.BaseSocket, ffm.TCPSocket);

})(ffm);
