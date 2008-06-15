import tkmr.*;
import java.net.*;
public class UPnPListenTest{
    public static void main(String[] args)
    {
        try{
            UPnP upnp = new UPnP();
            DatagramPacket packet = upnp.listenUDP(null, 30000);
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println(message);
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
}