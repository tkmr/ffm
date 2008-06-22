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

    public String read() throws IOException
    {
        return this.read(null);
    }
    public String read(ICallback callback) throws IOException
    {
        String result = "";
        String line;
        String callback_result;
        boolean isEndRead = false;
        while (((line = this.in.readLine()) != null) && !isEndRead) {
            System.out.println(line);
            if(callback != null){
                callback_result = callback.call(line);
                if(callback_result == "END"){
                    isEndRead = true;
                }else{
                    this.write(callback_result);
                }
            }
            result += line + "\uf8f8";
        }
        //Pattern pattern = Pattern.compile("\n");
        //Matcher matcher = pattern.matcher(result);
        //result = matcher.replaceAll("\uf8f8");
        return result;
    }

    public void close() throws IOException
    {
        this.out.close();
        this.in.close();
        this.socket.close();
    }
}