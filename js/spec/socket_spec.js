$(document).ready(function(){
    var connect_google = function(proc){
      try{
        var socket = new ffm.TCPSocket(80);
      }catch(e){
        ffm.Util.debug("Error: new ffm.TCPSocket(80)");
        ffm.Util.debug(e);
        return false;
      }
      socket.connect("www.google.com");
      socket.write("GET /webhp HTTP/1.1\r\nHost: www.google.com\r\nConnection: close\r\n\r\n");
      proc(socket);
      socket.close();
      return true;
    }

    test("ffm.TCPSocket can get response by readAll", function(){
        var pass = connect_google(function(socket){
            equals(socket.constructor, ffm.TCPSocket, "socket is ffm.Socket class instance");
            equals(socket.port, 80, "socket.port is 80");
            var result = socket.readAll();
            ok(result.match(/<html[^]*html>/i) !== null, "result.body include html pattern (<html> .... </html>)");
        });
        ok(pass, "success get a web page by socket.readall");
    });

    test("ffm.TCPSocket can get response by readline", function(){
        var pass = connect_google(function(socket){
            equals(socket.constructor, ffm.TCPSocket, "socket is ffm.Socket class instance");
            var result = [];
            var tmp = "";
            while((tmp = socket.readLine()) !== null){
              result.push(tmp);
            }
            ok(result.length > 1, "success read over one line");
            ok(result.join("").match(/<html[^]*html>/i) !== null, "result.body include html pattern (<html> .... </html>)");
        });
        ok(pass, "success get a web page by socket.readline");
    });

    test("ffm.TCPSocket can get response by read", function(){
        var pass = connect_google(function(socket){
            equals(socket.constructor, ffm.TCPSocket, "socket is ffm.Socket class instance");
            //HTTP/1.1 200 OK
            equals(socket.read(), "H", "get a H");
            equals(socket.read(), "T", "get a T");
            equals(socket.read(), "T", "get a T");
            equals(socket.read(), "P", "get a P");
        });
        ok(pass, "success get H T T P");
    });

    test("ffm.TCPSocket can get response by read(n)", function(){
        var pass = connect_google(function(socket){
            equals(socket.constructor, ffm.TCPSocket, "socket is ffm.Socket class instance");
            //HTTP/1.1 200 OK
            equals(socket.read(8), "HTTP/1.1", "get 'HTTP/1.1'");
            equals(socket.read(7), " 200 OK", "get ' 200 OK'");
        });
        ok(pass, "success get 'HTTP/1.1' and ' 200 OK'");
    });

    test("close the socket when call close method", function(){
        var socket = new ffm.TCPSocket(80);
        socket.connect("www.google.com", 5000);
        socket.close();
        ok(socket.isClosed(), "success close a socket");

        var socket2 = new ffm.TCPSocket(80);
        socket2.close();
        ok(socket2.isClosed(), "success close a socket");
    });
});
