package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class SocketListener
{
    public ICallback callback;
    public ISocket socket;

    public SocketListener(ISocket socket, ICallback callback)
    {
        this.socket = socket;
        this.callback = callback;
    }

    public void listen()
    {
        try{
            String result = this.socket.read(this.callback);
            System.out.println(result);
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                this.socket.close();
            } catch (IOException e) {}
        }
    }
}