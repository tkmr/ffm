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

    public String request(String host, String request, int timeout) throws IOException, SocketException
    {
        this.socket.connect(host, timeout);
        return this.request(request);
    }
    public String request(String request) throws IOException, SocketException
    {
        String result = "";
        try{
            this.socket.write(request);
            result = this.socket.readAll();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            this.socket.close();
        }
        return result;
    }

    public void asyncRequest(String host, String request, int timeout, String lastCallback, String updateCallback) throws IOException, SocketException
    {
        this.socket.connect(host, timeout);
        new SocketRequestThread(this, request, lastCallback, updateCallback).start();
    }
}
