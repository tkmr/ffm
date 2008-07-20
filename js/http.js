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
                       "User-Agent: Mozilla/5.0",
                       "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                       "Accept-Language: ja,en-us;q=0.7,en;q=0.3",
                       "Accept-Charset: utf-8;q=0.7,*;q=0.7",
                       "Connection: close",
                       "", ""];
    if(typeof(options.body) !== "undefined"){
      options.request.push(options.body);
    }
    var result = null;
    try{
      this.socket.connect(host[1], options.timeout, function(socket){
          console.log(options);
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
    console.log(result);
    var matches = result.match(/([^]*)(\n\n|\r\n\r\n)([^]*)/);
    if(matches === null){
      this.body = result;
    }else{
      var headers = matches[1].split("\n");
      this.header = {};
      for(var i in headers){
        var ht = headers[i].split(": ");
        this.header[ht[0]] = ht[1]
      }
      this.status = matches[1].match(new RegExp("HTTP/1.1 (.*) "));
      this.body = matches[2];
    }
  }

})(ffm);
