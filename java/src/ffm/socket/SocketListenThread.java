package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class SocketListenThread extends Thread
{
    private int port;
    private ICallback listenCallback;
    private ICallback threadCallback;
    private ISocket socket;

    public SocketListenThread(int port, ISocket socket, ICallback listenCallback, ICallback threadCallback)
    {
        this.port = port;
        this.socket = socket;
        this.listenCallback = listenCallback;
        this.threadCallback = threadCallback;
    }

    public void run()
    {
        ServerSocket serverSocket = null;
        int count = 0;
        try{
            serverSocket = new ServerSocket(this.port);
            while(this.threadCallback.call(Integer.toString(count)) != "END"){
                Socket basesocket = serverSocket.accept();
                ISocket socket = this.socket.generate((Object)basesocket);
                new SocketListenSubThread(socket, this.listenCallback).start();
                count += 1;
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {}
        }
    }

    public static Thread generate(int port, ISocket socket, ICallback listenCallback, ICallback threadCallback)
    {
        SocketListenThread thread = new SocketListenThread(port, socket, listenCallback, threadCallback);
        return thread;
    }
}