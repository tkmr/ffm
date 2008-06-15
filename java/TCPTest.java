import java.net.*;
import java.io.*;
import netscape.javascript.*;
import java.awt.event.*;
import java.awt.*;
import java.applet.*;

public class TCPTest extends Applet {
    public void init(){
        System.setProperty("java.security.policy", "http://localhost:8888/test.policy");
    }

    public void callback(String name)
    {

        return;
    }

    public String echo(String req)
    {
        return req;
    }

    public void listenTCP(int port)
    {
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println(port);

            Socket socket = serverSocket.accept();
            System.out.println(socket.getInetAddress() + "から接続を受付ました");
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);
            }
            System.out.println("処理が終了したので接続を切ります");

            out.close();
            in.close();
            socket.close();
            serverSocket.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public String getTCP(String host, int port, String request)
    {
        String result = "";
        try{
            Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(request);
            out.println(request);
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            out.close();
            in.close();
            socket.close();
        } catch(Exception e){
            result += e.toString();
        }
        return result;
    }

    public String getHTTP(String host, String path)
    {
        String request = "GET "+path+" HTTP/1.1\nHost: "+host+"\n\n";
        String result = getTCP(host, 80, request);
        return result;
    }
}