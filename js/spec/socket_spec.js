$(document).ready(function(){
    var connect_google = function(proc){
      try{
        var socket = new ffm.TCPSocket(80);
      }catch(e){
        ffm.Util.debug(e);
        return false;
      }
      socket.connect("www.google.com");
      socket.write("GET /webhp HTTP/1.1\r\nHost: www.google.com\r\nConnection: close\r\n\r\n");
      proc(socket);
      socket.close();
      return true;
    }

    test("ffm.TCPSocket can use TCP socket.", function(){
        var pass = connect_google(function(socket){
            equals(socket.constructor, ffm.TCPSocket, "socket is ffm.Socket class instance");
            equals(socket.port, 80, "socket.port is 80");
            var result = socket.readAll();
            ok(result.match(/<html[^]*html>/i) !== null, "result.body include html pattern (<html> .... </html>)");
        });
        ok(pass, "success connect to Google");
    });
});
