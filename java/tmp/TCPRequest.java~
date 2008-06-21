package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class TCPRequest
{
    private int port;
    private String host;
    private int timeout;

    public TCPRequest(String host, int port, int timeout)
    {
        this.port = port;
        this.host = host;
        this.timeout = timeout;
    }

    public String request(String request)
    {
        System.out.println(request);
        String result = "";
        try{
            InetSocketAddress address = new InetSocketAddress(this.host, this.port);
            Socket socket = new Socket();
            socket.connect(address, this.timeout);
            socket.setSoTimeout(this.timeout);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.write(request);
            out.flush();

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
}
