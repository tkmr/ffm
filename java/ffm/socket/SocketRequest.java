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

    public String request(String host, String[] requests, int timeout) throws IOException, SocketException
    {
        String request = Util.joinArray(requests);
        return this.request(host, request, timeout);
    }
    public String request(String host, String request, int timeout) throws IOException, SocketException
    {
        this.socket.connect(host, timeout);
        return this.request(request);
    }
    public String request(String request) throws IOException, SocketException
    {
        String result;
        try{
            this.socket.write(request);
            result = this.socket.read();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            this.socket.close();
        }
        return result;
    }

    public void asyncRequest(String host, String[] requests, int timeout, String callback)  throws IOException, SocketException
    {
        String request = Util.joinArray(requests);
        this.asyncRequest(host, request, timeout, callback);
    }
    public void asyncRequest(String host, String request, int timeout, String callback) throws IOException, SocketException
    {
        this.socket.connect(host, timeout);
        new ffm.js.SocketRequestThread(this, request, callback).start();
    }
}
