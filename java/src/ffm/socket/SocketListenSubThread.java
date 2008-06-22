package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class SocketListenSubThread extends Thread
{
    private ICallback callback;
    private ISocket socket;
    public SocketListenSubThread(ISocket socket, ICallback callback)
    {
        this.socket = socket;
        this.callback = callback;
    }

    public void run(){
        try{
            SocketListener listener = new SocketListener(this.socket, this.callback);
            listener.listen();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                this.socket.close();
            } catch (IOException e) {}
        }
    }
}