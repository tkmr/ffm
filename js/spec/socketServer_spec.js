$(document).ready(function(){
    var port = 20000 + Math.floor(Math.random() * 10000);
    var connect_server = function(proc){
      var socket = new ffm.TCPSocket(port);
      socket.connect("localhost", 10 * 1000);
      proc(socket);
    }

    var server = new ffm.SocketServer(new ffm.TCPSocket(port), function(subSocket){
        ffm.Util.debug("server");
        if(subSocket.readyRead()){
          var req = subSocket.readLine();
          ffm.Util.debug(req);
          subSocket.write("Request is :" + req + "\r\nDesu\r\n");
          subSocket.close();
          ok(subSocket.isClosed(), "server subSocket is closed");
        }
    });
    server.start();

    setTimeout(function(){
        test("ffm.SocketServer can accept TCP request", function(){
            connect_server(function(socket){
                ffm.Util.debug("client1");
                socket.write("hello world\r\n\r\n");
                stop();
                setTimeout(function(){
                    var result = socket.readLine();
                    socket.close();
                    equals(result, "Request is :hello world");
                    ok(socket.isClosed(), "socket is closed");
                }, 400);
            });
            connect_server(function(socket){
                ffm.Util.debug("client2");
                socket.write("hello byte\r\n\r\n");
                stop();
                setTimeout(function(){
                    var result = socket.read(22);
                    socket.close();
                    equals(result, "Request is :hello byte");
                    ok(socket.isClosed(), "socket is closed");
                }, 400);
            });
            setTimeout(function(){
                server.close();
                server.join();
                ok(server.isClosed(), "socket is closed");
                start();
            }, 3000);
        });
    }, 1000);
});
