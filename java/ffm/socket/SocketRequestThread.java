package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class SocketRequestThread extends Thread{
    private String message;
    private String callback;
    private SocketRequest socket;

    public SocketRequestThread(SocketRequest socket, String message, String callback){
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