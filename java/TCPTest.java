import java.net.*;
import java.io.*;
import netscape.javascript.*;
import java.awt.event.*;
import java.awt.*;
import java.applet.*;
import ffm.*;
import ffm.js.*;
import ffm.socket.*;

public class TCPTest extends Applet {

    public void init(){
        System.setProperty("java.security.policy", "http://myhost.com:8888/test.policy");
    }

    public TCPSocket createTCPSocket(int port)
    {
        return new TCPSocket(port);
    }

    public SocketRequest createSocketRequest(ISocket socket)
    {
        ICallback callback = new JSCallback("temp", (Applet)this);
        return new SocketRequest(socket, callback);
    }

    public String echo(String req)
    {
        return req;
    }
}