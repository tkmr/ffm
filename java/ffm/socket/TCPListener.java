package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;

public class TCPListener
{
    private Socket socket;
    private ICallback callback;

    public TCPListener(Socket socket, ICallback callback)
    {
        this.socket = socket;
        this.callback = callback;
    }

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
}