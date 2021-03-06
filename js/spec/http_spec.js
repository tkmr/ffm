$(document).ready(function(){
    var connect_google = function(proc){
      try{
        var socket = new ffm.TCPSocket(80);
        var http = new ffm.HTTP.Request(socket);
      }catch(e){
        ffm.Util.debug(e);
        return false;
      }
      proc(http);
      return true;
    }

    test("ffm.HTTP.Request can get a web page.", function(){
        var pass = connect_google(function(http){
            equals(http.constructor, ffm.HTTP.Request, "http is ffm.HTTP.Request class instance");
            equals(http.socket.constructor, ffm.TCPSocket, "http.socket is ffm.TCPSocket class instance");
            equals(http.socket.port, 80, "http.socket.port is 80");

            var result = http.get("http://www.google.co.jp/intl/ja/about.html");
            equals(result.constructor, ffm.HTTP.Response, "result is ffm.HTTP.Response class instance");

            var match = result.body.replace(/\n/,"").match(/<html[^]*html>/i);
            ok(match !== null, "result.body include html pattern (<html> .... </html>)");
            equals(result.status, "200");
            ok(http.socket.isClosed());
        });
        ok(pass, "get a web page 'http://www.google.co.jp/webhp'");
    });

    test("ffm.HTTP.Request generate a TCPSocket  when don't give a socket", function(){
        var http = new ffm.HTTP.Request();
        var result = http.get("http://www.google.co.jp/intl/ja/about.html");
        equals(http.socket.constructor, ffm.TCPSocket, "http.socket is ffm.TCPSocket class instance");
        equals(http.socket.port, 80, "http.socket.port is 80");
        ok(result.body.replace(/\n/,"").match(/<html[^]*html>/i) !== null, "result.body include html pattern (<html> .... </html>)");
        ok(http.socket.isClosed());
    });
});
