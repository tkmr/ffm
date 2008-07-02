package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class SocketListener
{
    public ICallback<ISocket> callback;
    public ISocket socket;

    public SocketListener(ISocket socket, ICallback<ISocket> callback)
    {
        this.socket = socket;
        this.callback = callback;
    }

    public void listen()
    {
        try{
            int count = 0;
            String result = this.callback.call(this.socket, Integer.toString(count));
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                this.socket.close();
            } catch (IOException e) {}
        }
    }
}