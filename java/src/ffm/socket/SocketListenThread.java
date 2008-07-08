package ffm.socket;
import ffm.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class SocketListenThread extends Thread
{
    private int port;
    private ISocket socket;
    private ServerSocket serverSocket;
    private ICallback<ISocket> listenCallback;
    private ArrayList<SocketListenSubThread> subThreads;

    public SocketListenThread(ISocket socket, ICallback<ISocket> listenCallback)
    {
        this.port = socket.getPort();
        this.socket = socket;
        this.listenCallback = listenCallback;
        this.subThreads = new ArrayList<SocketListenSubThread>();
    }

    public void run()
    {
        try{
            this.serverSocket = new ServerSocket(this.port);
            while(!this.isClosed()){
                if(!this.isClosed()){
                    Socket basesocket = this.serverSocket.accept();
                    ISocket socket = this.socket.generate((Object)basesocket);
                    SocketListenSubThread subThread = new SocketListenSubThread(socket, this.listenCallback);
                    subThread.start();
                    subThreads.add(subThread);
                }
            }
        } catch(SocketException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            if (this.serverSocket != null && !this.isClosed()) {
                this.close();
            }
        }
    }

    public void close()
    {
        this.close(true);
    }
    public void close(Boolean closeSubThread)
    {
        if(!this.isClosed()){
            try{
                this.serverSocket.close();
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if(closeSubThread != null && closeSubThread){
                    for(SocketListenSubThread thread : this.subThreads){
                        thread.close();
                    }
                }
            }
        }
    }

    public Boolean isClosed()
    {
        if(this.serverSocket == null){ return true; }
        return this.serverSocket.isClosed();
    }

    public ArrayList<SocketListenSubThread> getSubThreads()
    {
        return this.subThreads;
    }
}