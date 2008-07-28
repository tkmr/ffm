package ffm;
import java.net.*;
import java.io.*;
import netscape.javascript.*;
import java.awt.event.*;
import java.awt.*;
import java.applet.*;
import ffm.*;
import ffm.js.*;
import ffm.socket.*;
import java.util.ArrayList;

public class JSocketApplet extends Applet
{
    private ArrayList<SocketListenThread> socketlistenthreads = new ArrayList<SocketListenThread>();
    public void init()
    {
        System.out.println("start!!");
        //String policyfile = getParameter("policy");
        String policyfile = "http://myhost.com:8888/test.policy";
        System.out.println(policyfile);
        System.setProperty("java.security.policy", policyfile);
        System.setSecurityManager( new SecurityManager());
        System.out.println("setup end");
    }

    public void paint(Graphics g){
        Dimension size = getSize();
        g.setColor(Color.blue);
        g.fillRect(0, 0, size.width - 1, size.height - 1);
    }

    public void stop()
    {
        this.close();
    }
    public void destroy()
    {
        this.close();
    }
    public void close()
    {
        for(SocketListenThread thread : this.socketlistenthreads){
            thread.close();
        }
    }

    public TCPSocket createTCPSocket(int port)
    {
        return new TCPSocket(port);
    }

    public SocketListenThread createSocketListenThread(ISocket socket, String listenFunc)
    {
        ICallback<ISocket> listenCallback = new JSCallback<ISocket>(listenFunc, (Applet)this);
        SocketListenThread thread = new SocketListenThread(socket, listenCallback);
        this.socketlistenthreads.add(thread);
        return thread;
    }
}