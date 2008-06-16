package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class TCPListenThread extends Thread{
    private int port;
    private ICallback callback;

    public TCPListenThread(int port, ICallback callback){
        this.port = port;
        this.callback = callback;
    }

    public void run(){
        ServerSocket serverSocket = null;
        int i = 0;
        try{
            serverSocket = new ServerSocket(this.port);
            while(i < 10){
                Socket socket = serverSocket.accept();
                new TCPListenSubThread(socket, this.callback).start();
                i += 1;
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