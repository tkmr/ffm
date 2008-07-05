package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class SocketListenThread extends Thread
{
    private int port;
    private ICallback<ISocket> listenCallback;
    private ICallback<Integer> threadCallback;
    private ISocket socket;

    public SocketListenThread(ISocket socket, ICallback<ISocket> listenCallback, ICallback<Integer> threadCallback)
    {
        this.port = socket.getPort();
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
            while(this.threadCallback.call(count) != null){
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
}