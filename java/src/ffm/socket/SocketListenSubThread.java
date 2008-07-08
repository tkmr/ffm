package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class SocketListenSubThread extends Thread
{
    private ICallback<ISocket> callback;
    private ISocket socket;

    public SocketListenSubThread(ISocket socket, ICallback<ISocket> callback)
    {
        this.socket = socket;
        this.callback = callback;
    }

    public void run()
    {
        try{
            String result = this.callback.call(this.socket);
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                this.socket.close();
            } catch (IOException e) {}
        }
    }

    public void close()
    {
        if(!this.isClosed()){
            try{
                this.socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public boolean isClosed()
    {
        if(this.socket == null){ return true; }
        return this.socket.isClosed();
    }
}