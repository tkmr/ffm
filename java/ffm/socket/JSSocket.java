package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class JSSocket
{
    public ICallback callback;
    public ISocket socket;
    public String host;
    public int timeout;

    public JSSocket(ISocket socket, ICallback callback)
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
        this.socket.write(request);
        result = this.socket.read();
        this.socket.close();
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
        new JSSocketRequestThread(this, request, callback).start();
    }
}

class JSSocketRequestThread extends Thread{
    private String message;
    private String callback;
    private JSSocket socket;

    public JSSocketRequestThread(JSSocket socket, String message, String callback){
        this.message = message;
        this.socket = socket;
        this.callback = callback;
    }

    public void run()
    {
        try{
            String result = this.socket.request(message);
            String jsResult = this.socket.callback.call(result, this.callback);
        }catch(Exception exp){
            String jsReqult = this.socket.callback.call(exp.toString(), this.callback);
        }
    }
}