import tkmr.*;
import java.net.*;

public class UPnPTest{
    public static void main(String[] args)
    {
        try{
            UPnP upnp = new UPnP();
            String message = upnp.discoverRouter("239.255.255.250", 1900);
            message = upnp.getLocation(message);
            System.out.println(message);
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
}