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
        try{
            socket.socket = basesocket;
            socket.setIO();
        }catch(IOException e){
            socket.close();
            throw e;
        }
        return (ISocket)socket;
    }

    public void connect(String host, int timeout) throws IOException, SocketException
    {
        try{
            InetSocketAddress address = new InetSocketAddress(host, this.port);
            this.socket.connect(address, timeout);
            this.socket.setSoTimeout(timeout);
            this.setIO();
        }catch(IOException e){
            this.close();
            throw e;
        }
    }

    private void setIO() throws IOException
    {
        this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void close() throws IOException
    {
        try{
            if(this.out != null){
                this.out.close();
            }
            if(this.in != null){
                this.in.close();
            }
        }finally{
            if(this.socket != null){
                this.socket.close();
            }
        }
    }

    public boolean isClosed()
    {
        return this.socket.isClosed();
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
        try{
            this.out.write(request);
            this.out.flush();
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