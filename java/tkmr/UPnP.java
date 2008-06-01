package tkmr;

//import java.awt.*;
//import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.regex.*;

public class UPnP{
    private static int BUFFER_SIZE = 100000;
    private static String SSDP_ST_SERVICE = "ST: urn:schemas-upnp-org:service:WANPPPConnection:1";
    private static String SSDP_ST_DEVICE  = "ST: urn:schemas-upnp-org:device:InternetGatewayDevice:1";
    public int usePort;
    public DatagramPacket sendPacket;
    public DatagramPacket receivePacket;
    public DatagramSocket sendSocket;

    public UPnP()
    {

    }

    public DatagramPacket listenUDP(int port, int timeout) throws SocketTimeoutException, IOException
    {
        byte[] data = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(data, BUFFER_SIZE);
        DatagramSocket socket = new DatagramSocket(null);
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
        socket.setSoTimeout(timeout);
        socket.receive(packet);
        return packet;
    }

    public String discoverRouter(String host, int port) throws SocketTimeoutException, IOException, InterruptedException
    {
        InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
        byte[] sendBuffer =
            ("M-SEARCH * HTTP/1.1\r\n" +
             "HOST: 239.255.255.250:1900\r\n" +
             "MAN: \"ssdp:discover\"\r\n" +
             "MX: 1\r\n" +
             SSDP_ST_DEVICE + "\r\n\r\n").getBytes();

        sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, remoteAddress);
        sendSocket = new DatagramSocket();
        sendSocket.setReuseAddress(true);
        usePort = sendSocket.getLocalPort();

        Thread t1 = new Thread(){
                public void run(){
                    try{
                        sleep(100);
                        sendSocket.send(sendPacket);
                    }catch(Exception e){
                    }
                }
        };
        Thread t2 = new Thread(){
                public void run(){
                    try{
                        receivePacket = listenUDP(usePort, 3000);
                    }catch(Exception e){
                    }
                }
        };

        t2.start();
        t1.start();

        t1.join();
        t2.join();

        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
        return message;
    }

    public String getLocation(String ssdpMessage)
    {
        Pattern p = Pattern.compile("LOCATION: (.*)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(ssdpMessage);
        m.find();
        return m.group(1);
    }
}