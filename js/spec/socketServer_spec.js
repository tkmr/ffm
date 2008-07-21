$(document).ready(function(){
  var sub = frames[0].window;

  var wait = function(callback){
    var timer = {};
    timer["sub"] = setInterval(function(){
        if(sub.ffm !== undefined){
          clearInterval(timer["sub"]);
          callback();
        }
    }, 500);
  }

  wait(function(){
    console.log(frames[0].window.ffm);
    var port = 20000 + Math.floor(Math.random() * 10000);
    var connect_server = function(proc){
      var socket = new ffm.TCPSocket(port);
      socket.connect("localhost", 10 * 1000);
      proc(socket);
      socket.close();
      ok(socket.isClosed(), "socket is closed");
    }

    test("ffm.SocketServer can accept TCP request", function(){
        var server = new sub.ffm.SocketServer(new sub.ffm.TCPSocket(port), function(subSocket){
            if(subSocket.readyRead()){
              var req = subSocket.readLine();
              ffm.Util.debug(req);
              subSocket.write("Request is :" + req + "\r\nDesu\r\n");
            }
        });
        server.start();

        connect_server(function(socket){
            socket.write("hello world\r\n\r\n");
            var result = socket.readLine();
            ffm.Util.debug(result);
            equals(result, "Request is :hello world");
        });

        connect_server(function(socket){
            socket.write("hello byte\r\n\r\n");
            var result = socket.read(22);
            ffm.Util.debug(result);
            equals(result, "Request is :hello byte");
        });

        server.close();
        server.join();
        ok(server.isClosed(), "socket is closed");
    });
  });
});
