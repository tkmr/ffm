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
        System.setProperty("java.security.policy", "http://localhost:8888/test.policy");
    }

    public String echo(String req)
    {
        return req;
    }

    public String requestHTTP(String host, String path)
    {
        String message = "GET "+path+" HTTP/1.1\r\nHost: "+host+"\r\nConnection: close\r\n\r\n";
        TCPRequest tcp = new TCPRequest(host, 80, 3000);
        String result = tcp.request(message);
        return result;
    }

    public void requestHTTPAsync(String host, String path, String jscallback)
    {
        String message = "GET "+path+" HTTP/1.1\r\nHost: "+host+"\r\nConnection: close\r\n\r\n";
        JSCallback callback = new JSCallback(jscallback, (Applet)this);
        TCPRequest tcp = new TCPRequest(host, 80, 3000);
        new TCPRequestThread(tcp, message, callback).start();
    }

    public void listenTCP(int port, String jscallback)
    {
        JSCallback callback = new JSCallback(jscallback, (Applet)this);
        new TCPListenThread(port, callback).start();
    }
}