package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

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

    public void connect(String host, int timeout) throws IOException, SocketException
    {
        InetSocketAddress address = new InetSocketAddress(host, this.port);
        this.socket.connect(address, timeout);
        this.socket.setSoTimeout(timeout);

        this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void write(String[] requests) throws IOException
    {
        String request = "";
        for(int i = 0; i < requests.length; i++){
            request += requests[i] + "\r\n";
        }
        this.write(request);
    }

    public void write(String request) throws IOException
    {
        this.out.write(request);
        this.out.flush();
    }

    public String read() throws IOException
    {
        String result = "";
        String line;
        while ((line = this.in.readLine()) != null) {
            System.out.println(line);
            result += line;
        }
        return result;
    }

    public void close() throws IOException
    {
        this.out.close();
        this.in.close();
        this.socket.close();
    }

    /*
    public void listen()
    {
        try{
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String inputLine;
            String inputLines = "";
            while ((inputLine = in.readLine()) != null) {
                inputLines += inputLine;
            }

            String outputLines = this.callback.call(inputLines);
            out.println(outputLines);

            out.close();
            in.close();
            this.socket.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    */
}