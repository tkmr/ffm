package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class SocketSimpleRequestThread extends Thread{
    private String lastCallbackName;
    private SocketSimpleRequest socket;

    public SocketSimpleRequestThread(SocketRequest socket, String lastCallback){
        this.socket = socket;
        this.lastCallbackName = lastCallback;
    }

    public void run()
    {
        try{
            String result = this.socket.socket.readAll();
            String jsResult = this.socket.callback.call(result, this.lastCallbackName);
        }catch(Exception exp){
            //String jsReqult = this.socket.callback.call(exp.toString(), this.callback);
        }
    }
}