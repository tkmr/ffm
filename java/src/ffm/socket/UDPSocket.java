package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class UDPSocket implements ISocket
{
    private int defaultTimeout = 1000; //UDPSocket is not use..
    private int port;
    private Socket socket;
    private String host;

    public UDPSocket(int port)
    {
        this.port = port;
        this.socket = new DatagramSocket();
    }

    /*
    public ISocket generate(Object rawsocket) throws IOException
    {
        Socket basesocket = (Socket)rawsocket;
        TCPSocket socket = new TCPSocket(basesocket.getLocalPort());
        try{
            socket.socket = basesocket;
            socket.setIO();
        }catch(IOException e){
            socket.close();
            throw e;
        }
        return (ISocket)socket;
    }
    */

    public void connect(String host) throws IOException, SocketException
    {
        this.connect(host, defaultTimeout);
    }
    public void connect(String host, int timeout) throws IOException, SocketException
    {
        this.host = host;
    }

    public void close() throws IOException
    {
        if(this.socket != null){
            this.socket.close();
        }
    }

    public boolean isClosed()
    {
        return this.socket.isClosed();
    }

    public int getPort()
    {
        return this.port;
    }

    //Write //////////////////////////////////////////////////////////////////////////////////
    public void write(String[] requests) throws IOException
    {
        try{
            String request = Util.joinArray(requests);
            this.write(request);
        }catch(IOException e){
            this.close();
            throw e;
        }
    }
    public void write(String request) throws IOException
    {
        byte buf[] = request.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(this.host), this.port);
        try{
            this.socket.send(packet);
        }catch(IOException e){
            this.close();
            throw e;
        }
    }

    //Read //////////////////////////////////////////////////////////////////////////////////
    public String readAll() throws IOException
    {
        String result = "";
        String line;
        try{
            while ((line = this.in.readLine()) != null) {
                //result += line + "\uf8f8";
                result += line + "\r\n";
            }
        }catch(IOException e){
            this.close();
            throw e;
        }
        return result;
    }

    public String readLine() throws IOException
    {
        String result;
        try{
            result = this.in.readLine();
        }catch(IOException e){
            this.close();
            throw e;
        }
        return result;
    }

    public char read() throws IOException
    {
        char result;
        try{
            result = (char)this.in.read();
        }catch(IOException e){
            this.close();
            throw e;
        }
        return result;
    }

    public String read(int length) throws IOException
    {
        char[] result = new char[length];
        try{
            int size = this.in.read(result, 0, length);
        }catch(IOException e){
            this.close();
            throw e;
        }
        String results = new String(result);
        return results;
    }
}