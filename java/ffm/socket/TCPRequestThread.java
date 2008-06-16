package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class TCPRequestThread extends Thread{
    private String message;
    private TCPRequest tcp;
    private ICallback callback;

    public TCPRequestThread(TCPRequest socket, String message, ICallback callback){
        this.message = message;
        this.tcp = socket;
        this.callback = callback;
    }

    public void run(){
        String result = this.tcp.request(message);
        String jsResult = this.callback.call(result);
    }
}
