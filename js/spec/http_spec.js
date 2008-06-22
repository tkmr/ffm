$(document).ready(function(){
    test("ffm.HTTP.Request class can get a web page.", function(){
        var pass=true;
        try{
          var http = new ffm.HTTP.Request();
        }catch(e){
          pass = false;
        }
        ok(pass, "get a HTTPRequest instance");
        equals(http.constructor, ffm.HTTP.Request, "http is ffm.HTTP.Request class instance");
        equals(http.requester.constructor, ffm.SocketRequest, "http.requester is ffm.SocketRequest class instance");
        //equals(http.requester.socket.constructor, ffm.TCPSocket, "http.requester.socket is ffn.TCPSocket class instance");
        equals(http.requester.socket.port, 80, "http.requester.socket.port is 80");

        pass=true;
        try{
          var result = http.get("http://www.google.com");
        }catch(e){
          pass = false;
        }
        ok(pass, "get a web page 'http://www.google.com/'");
        ok(result.body.match(/<html[^]*html>/i) !== null, "result.body include html pattern (<html> .... </html>)");
        equals(result.status, "302");
    });
});
