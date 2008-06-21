package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class SocketServer
{
    public ICallback callback;
    public ISocket socket;
    public int listen_timeout;

    public SocketServer(ISocket socket, ICallback callback)
    {
        this.socket = socket;
        this.callback = callback;
    }
}