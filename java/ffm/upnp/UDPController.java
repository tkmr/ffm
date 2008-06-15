package ffm.upnp;
import java.net.*;
import java.io.*;
import java.util.regex.*;

public class UDPController{
    private final int BUFFER_SIZE = 100000;
    private final String SSDP_ST_DEVICE    = "urn:schemas-upnp-org:device:InternetGatewayDevice:1";
    private final String SSDP_HOST = "239.255.255.250";
    private final int SSDP_PORT = 1900;
    private final byte[] SSDP_UDP_REQUEST =
        ("M-SEARCH * HTTP/1.1\r\n" +
         "HOST: 239.255.255.250:1900\r\n" +
         "MAN: \"ssdp:discover\"\r\n" +
         "MX: 1\r\n" +
         "ST: " + SSDP_ST_DEVICE + "\r\n\r\n").getBytes();

    private int usePort;
    private DatagramPacket udpReceivePacket;
    private DatagramPacket sendPacket;
    private DatagramSocket sendSocket;

    public UDPController()
    {
    }

    public DatagramPacket listenUDP(int port, int timeout, int buffersize) throws SocketTimeoutException, IOException
    {
        byte[] data = new byte[buffersize];
        DatagramPacket packet = new DatagramPacket(data, buffersize);
        DatagramSocket socket = new DatagramSocket(null);
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
        socket.setSoTimeout(timeout);
        socket.receive(packet);
        return packet;
    }

    public DatagramPacket discoverRouter() throws SocketTimeoutException, IOException, InterruptedException
    {
        InetSocketAddress remoteAddress = new InetSocketAddress(SSDP_HOST, SSDP_PORT);
        sendPacket = new DatagramPacket(SSDP_UDP_REQUEST, SSDP_UDP_REQUEST.length, remoteAddress);
        sendSocket = new DatagramSocket();
        sendSocket.setReuseAddress(true);
        usePort = sendSocket.getLocalPort();

        Thread reciever = new Thread(){
                public void run(){
                    try{
                        udpReceivePacket = listenUDP(usePort, 3000, BUFFER_SIZE);
                    }catch(Exception e){
                    }
                }
        };
        Thread sender = new Thread(){
                public void run(){
                    try{
                        sleep(300);
                        sendSocket.send(sendPacket);
                    }catch(Exception e){
                    }
                }
        };

        reciever.start();
        sender.start();
        reciever.join();
        sender.join();

        return udpReceivePacket;
    }

    public String discoverRouterLocation() throws SocketTimeoutException, IOException, InterruptedException
    {
        DatagramPacket receivePacket = discoverRouter();
        //TODO: ReceivePacket check
        if(receivePacket == null){
            return null;
        }
        String udpMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        Pattern p = Pattern.compile("LOCATION: (.*)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(udpMessage);
        m.find();
        return m.group(1);
    }

    /*
    public String discoverRouterControlURL()
    {
        String location = discoverRouterLocation();
        //TODO: Null check;
        if(location == null){
            return null;
        }

        URL ssdpUrl = new URL(location);
        Document doc = Util.getXMLHttp(ssdpUrl.toString());
        //TODO: Null check;
        if(doc == null){
            return null;
        }

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        NodeList resultNodes = (NodeList)xpath.evaluate("//service/serviceType", doc, XPathConstants.NODESET);

        String content;
        Element parent = null;
        Pattern pattern = Pattern.compile(SSDP_ST_IPSERVICE+"|"+SSDP_ST_SERVICE);
        for(int i = 0; i < resultNodes.getLength(); i++){
            content = resultNodes.item(i).getTextContent();
            if(pattern.matcher(content).find()){
                parent = (Element)resultNodes.item(i).getParentNode();
            }
        }

        URL controlURL;
        if(parent != null){
            String controlUrlPath = parent.getElementsByTagName("controlURL").item(0).getTextContent();
            controlURL = new URL(ssdpUrl.getProtocol(), ssdpUrl.getHost(), ssdpUrl.getPort(), controlUrlPath);
        }
        return controlURL;
    }
    */
}