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
  ffm.BaseSocket.prototype.connect = function(host, timeout){
    timeout = timeout || ffm.Config.defaultTimeout;
    this.socket.connect(host, timeout);
  }
  ffm.BaseSocket.prototype.write = function(request){
    this.socket.write(request);
  }
  ffm.BaseSocket.prototype.read = function(){
    var result = this.socket.read();
    return ffm.Util.F8F8toCRLF(result);
  }
  ffm.BaseSocket.prototype.close = function(){
    this.socket.close();
  }


  ffm.TCPSocket = function(port, applet){
    this.init(applet);
    this.port = port;
    this.socket = this.applet.createTCPSocket(port);
  }
  ffm.TCPSocket.prototype = ffm.BaseSocket.prototype;


  ////////////////////////////////////////////////////////////////////////////
  /*
   * Socket request (sync or async)
   */
  ffm.SocketRequest = function(jssocket){
    jssocket = jssocket || new ffm.TCPSocket(80);
    this.socket = jssocket;
    this.javarequester = jssocket.applet.createSocketRequest(jssocket.socket);
  }
  ffm.SocketRequest.prototype.request = function(host, options){
    options = options || {};
    options.fail    = options.fail || (function(){});
    options.timeout = options.timeout || ffm.Config.defaultTimeout;
    options.request = options.request || "";
    if(typeof(options.request) !== "string"){
      var request = java.lang.reflect.Array.newInstance(java.lang.String, options.request.length);
      for(var i=0; i < options.request.length; i++){
        request[i] = options.request[i];
      }
      options.request = request;
    }
    if(typeof(options.success) === "function"){
      var successTempName = "jsocket_temp_function_" + Math.floor(Math.random() * 1000000000).toString();
      window[successTempName] = function(result){
        result = ffm.Util.F8F8toCRLF(result);
        options.success(result);
      }
      try{
        return this.javarequester.asyncRequest(host, options.request, options.timeout, successTempName);
      }catch(e){
        options.fail();
      }
    }else{
      var res = this.javarequester.request(host, options.request, options.timeout);
      return ffm.Util.F8F8toCRLF(res);
    }
  }

})(ffm);
