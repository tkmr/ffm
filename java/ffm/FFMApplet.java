package ffm;
//import netscape.javascript.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import ffm.upnp.*;

public class FFMApplet extends Applet{
    public void init(){
    }

    public String getRouterControlURL()
    {
        try{
            UDPController udpClient = new UDPController();
            String location = udpClient.discoverRouterLocation();
            SOAPController soapClient = new SOAPController(location);
            return soapClient.getRouterURL();
        }catch(Exception e){
            return null;
        }
    }
}
