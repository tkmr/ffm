var ffm = ffm||{};

(function(ffm){
  ffm.HTTP = ffm.HTTP||{};

  ////////////////////////////////////////////////////////////////////////////
  /*
   * HTTP request
   */
  ffm.HTTP.Request = function(socket){
    socket = socket || new ffm.TCPSocket(80);
    this.socket = socket;
  }
  ffm.HTTP.Request.prototype.request = function(url, method, options){
    options = options || {};
    options.timeout = options.timeout || ffm.Config.defaultTimeout;
    var match = url.match(new RegExp("https?://([^/]*)(.*)"));
    var host = match[1].match(/([^:]*)(.*)/);
    var path = match[2] || "/";
    options.request = [method+" "+path+" HTTP/1.1",
                       "Host: "+host[1],
                       "Connection: close",
                       ""];
    if(typeof(options.body) !== "undefined"){
      options.request.push(options.body);
    }
    var result = null;
    try{
      this.socket.connect(host[1], options.timeout, function(socket){
          socket.write(options.request);
          result = socket.readAll();
      });
    }catch(e){
      ffm.Util.debug(e);
      this.socket.close();
      return null;
    }
    if(result === null || typeof(result) === "undefined"){
      return null;
    }else{
      return new ffm.HTTP.Response(result);
    }
  }
  ffm.HTTP.Request.prototype.get = function(url, options){
    return this.request(url, "GET", options);
  }
  ffm.HTTP.Request.prototype.post = function(url, body, options){
    options = options || {};
    options.body = body;
    return this.request(url, "POST", options);
  }
  ffm.HTTP.Request.prototype.put = function(url, body, options){
    options = options || {};
    options.body = body;
    return this.request(url, "PUT", options);
  }
  ffm.HTTP.Request.prototype.delete = function(url, options){
    return this.request(url, "DELETE", options);
  }


  ////////////////////////////////////////////////////////////////////////////
  /*
   * HTTP response
   */
  ffm.HTTP.Response = function(result){
    var results = result.split(/\r\n|\n/);
    var endHeader = false;
    this.header = {};
    this.header.status = results.shift();
    this.status = this.header.status.match(new RegExp("HTTP/1.1 (.*) "))[1];
    var body = "";

    var i = results.shift();
    while(i !== undefined){
      if(i===""){ endHeader = true; }
      if(!endHeader){
        var tmp = i.match(/^([^:]*): (.*)$/);
        this.header[tmp[1]] = tmp[2];
      }else{
        body += i + "\n";
      }
      i = results.shift();
    }
    this.body =  body;
  }

})(ffm);
