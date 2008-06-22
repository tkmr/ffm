$(document).ready(function(){
    test("ffm.TCPSocket class can use TCP socket.", function(){
        var pass=true;
        try{
          var socket = new ffm.TCPSocket(80);
        }catch(e){
          pass = false;
        }
        ok(pass, "get a TCPSocket instance");
        equals(socket.port, 80, "socket.port is 80");

        pass=true;
        try{
          socket.connect("www.google.com", 5000);
          socket.write(["GET / HTTP/1.1", "Host: www.google.com", "Connection: close", ""]);
          var result = socket.read();
          socket.close();
        }catch(e){
          pass = false;
        }
        ok(pass, "get a web page 'http://www.google.com/'");
        ok(result.match(/<html[^]*html>/i) !== null, "result.body include html pattern (<html> .... </html>)");
    });
});
