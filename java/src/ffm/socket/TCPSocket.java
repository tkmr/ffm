package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;
import java.util.regex.*;

public class TCPSocket implements ISocket
{
    public BufferedWriter out;
    public BufferedReader in;
    public int port;
    public Socket socket;

    public TCPSocket(int port)
    {
        this.port = port;
        this.socket = new Socket();
    }

    public ISocket generate(Object rawsocket) throws IOException
    {
        Socket basesocket = (Socket)rawsocket;
        TCPSocket socket = new TCPSocket(basesocket.getLocalPort());
        socket.socket = basesocket;
        socket.setIO();
        return (ISocket)socket;
    }

    public void connect(String host, int timeout) throws IOException, SocketException
    {
        InetSocketAddress address = new InetSocketAddress(host, this.port);
        this.socket.connect(address, timeout);
        this.socket.setSoTimeout(timeout);
        this.setIO();
    }

    public void setIO() throws IOException
    {
        this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void close() throws IOException
    {
        this.out.close();
        this.in.close();
        this.socket.close();
    }

    //Write //////////////////////////////////////////////////////////////////////////////////
    public void write(String[] requests) throws IOException
    {
        String request = Util.joinArray(requests);
        this.write(request);
    }
    public void write(String request) throws IOException
    {
        System.out.println(request);
        this.out.write(request);
        this.out.flush();
    }

    //Read //////////////////////////////////////////////////////////////////////////////////
    public String readAll() throws IOException
    {
        String result = "";
        String line;
        while ((line = this.in.readLine()) != null) {
            //result += line + "\uf8f8";
            result += line + "\r\n";
        }
        return result;
    }

    public String readLine() throws IOException
    {
        return this.in.readLine();
    }

    public char read() throws IOException
    {
        return (char)this.in.read();
    }

    public String read(int length) throws IOException
    {
        char[] result = new char[length];
        int size = this.in.read(result, 0, length);
        String results = new String(result);
        return results;
    }
}