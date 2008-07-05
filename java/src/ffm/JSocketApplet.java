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

public class JSocketApplet extends Applet
{
    public void init()
    {
        String policyfile = getParameter("policy");
        System.setProperty("java.security.policy", policyfile);
    }

    public TCPSocket createTCPSocket(int port)
    {
        return new TCPSocket(port);
    }

    public SocketListenThread createSocketListenThread(ISocket socket, String listenFunc, String threadFunc)
    {
        ICallback<ISocket> listenCallback = new JSCallback<ISocket>(listenFunc, (Applet)this);
        ICallback<Integer> threadCallback = new JSCallback<Integer>(threadFunc, (Applet)this);
        SocketListenThread thread = new SocketListenThread(socket, listenCallback, threadCallback);
        return thread;
    }
}