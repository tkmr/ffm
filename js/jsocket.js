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
    return this.socket.read();
  }
  ffm.BaseSocket.prototype.close = function(){
    this.socket.close();
  }


  ffm.TCPSocket = function(port, applet){
    this.init(applet);
    this.socket = this.applet.createTCPSocket(port);
  }
  ffm.TCPSocket.prototype = ffm.BaseSocket.prototype;


  ////////////////////////////////////////////////////////////////////////////
  /*
   * Socket request (sync or async)
   */
  ffm.SocketRequest = function(jssocket){
    jssocket = jssocket || new ffm.TCPSocket(80);
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
      window[successTempName] = options.success;
      try{
        return this.javarequester.asyncRequest(host, options.request, options.timeout, successTempName);
      }catch(e){
        options.fail();
      }
    }else{
      return this.javarequester.request(host, options.request, options.timeout);
    }
  }


  ////////////////////////////////////////////////////////////////////////////
  /*
   * HTTP request
   */
  ffm.HTTPRequest = function(socketRequest){
    socketRequest = socketRequest || new ffm.SocketRequest();
    this.requester = socketRequest;
  }
  ffm.HTTPRequest.prototype.request = function(url, method, options){
    options = options || {};
    options.timeout = options.timeout || ffm.Config.defaultTimeout;
    var match = url.match(new RegExp("https?://([^/]*)(.*)"));
    var host = match[1];
    var path = match[2] || "/";
    options.request = [method+" "+path+" HTTP/1.1",
                       "host: "+host,
                       "User-Agent: Mozilla/5.0",
                       "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                       "Accept-Language: ja,en-us;q=0.7,en;q=0.3",
                       "Accept-Charset: Shift_JIS,utf-8;q=0.7,*;q=0.7",
                       "Connection: close",
                       "", ""];
    if(typeof(options.body) !== "undefined"){
      options.request.push(options.body);
    }
    var result = this.requester.request(host, options);
    if(result === null || typeof(result) === "undefined"){
      return null;
    }else{
      return new ffm.HTTPResponse(result);
    }
  }
  ffm.HTTPRequest.prototype.get = function(url, options){
    return this.request(url, "GET", options);
  }
  ffm.HTTPRequest.prototype.post = function(url, body, options){
    options = options || {};
    options.body = body;
    return this.request(url, "POST", options);
  }
  ffm.HTTPRequest.prototype.put = function(url, body, options){
    options = options || {};
    options.body = body;
    return this.request(url, "PUT", options);
  }
  ffm.HTTPRequest.prototype.delete = function(url, options){
    return this.request(url, "DELETE", options);
  }


  ////////////////////////////////////////////////////////////////////////////
  /*
   * HTTP response
   */
  ffm.HTTPResponse = function(result){
    var matches = result.match(/^(.*)\n\n(.*)$/);
    if(matches === null){
      this.body = result;
    }else{
      var headers = matches[1].split("\n");
      this.header = {};
      for(var header in headers){
        this.header[key] = header.split(": ");
      }
      this.status = this.header[0].match(new RegExp("HTTP/1.1 (.*) "))[1];
      this.body = matches[2];
    }
  }


})(ffm);
