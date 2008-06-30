package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class SocketRequest
{
    public ICallback callback;
    public ISocket socket;
    public String host;
    public int timeout;

    public SocketRequest(ISocket socket, ICallback callback)
    {
        this.socket = socket;
        this.callback = callback;
    }

    public String simpleRequest(String host, String request, int timeout) throws IOException, SocketException, Exception
    {
        String result = "";
        try{
            this.socket.connect(host, timeout);
            this.socket.write(request);
            result = this.socket.readAll();
        }catch(Exception e){
            this.socket.close();
            throw e;
        }
        return result;
    }

    public void asyncSimpleRequest(String host, String request, int timeout, String lastCallback) throws IOException, SocketException, Exception
    {
        try{
            this.socket.connect(host, timeout);
            this.socket.write(request);
        }catch(Exception e){
            this.socket.close();
            throw e;
        }
        new SocketSimpleRequestThread(this, lastCallback).start();
    }

    public void asyncInteractRequest(String host, int timeout, String lastCallback, String updateCallback) throws IOException, SocketException, Exception
    {
        try{
            this.socket.connect(host, timeout);
        }catch(Exception e){
            this.socket.close();
            throw e;
        }
        new SocketInteractRequestThread(this, lastCallback, updateCallback).start();
    }
}
