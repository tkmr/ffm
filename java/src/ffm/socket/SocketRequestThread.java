package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class SocketRequestThread extends Thread{
    private String message;
    private String lastCallbackName;
    private String updateCallbackName;
    private SocketRequest socket;

    public SocketRequestThread(SocketRequest socket, String message, String lastCallback, String updateCallback){
        this.message = message;
        this.socket = socket;
        this.lastCallbackName = lastCallback;
        this.updateCallbackName = updateCallback;
    }

    public void run()
    {
        try{
            String result = this.socket.request(message);
            String jsResult = this.socket.callback.call(result, this.lastCallbackName);
        }catch(Exception exp){
            //String jsReqult = this.socket.callback.call(exp.toString(), this.callback);
        }
    }
}