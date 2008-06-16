package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class TCPListenSubThread extends Thread
{
    private ICallback callback;
    private Socket socket;
    public TCPListenSubThread(Socket socket, ICallback callback)
    {
        this.socket = socket;
        this.callback = callback;
    }

    public void run(){
        TCPListener listener = new TCPListener(socket, callback);
        listener.listen();
    }
}