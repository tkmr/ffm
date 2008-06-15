import tkmr.upnp.*;
import java.util.regex.*;
import java.net.*;

public class UPnPTest{
    public static void main(String[] args){
        try{
            UDPController udpClient = new UDPController();
            String location = udpClient.discoverRouterLocation();
            System.out.println(location);

            SOAPController soapClient = new SOAPController(location);
            System.out.println(soapClient.getRouterURL());
						
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
}