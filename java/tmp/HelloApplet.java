import netscape.javascript.*;
import java.awt.*;
import java.applet.*;
import ffm.upnp.*;

public class HelloApplet extends Applet {
    private String routerURL;
    public void init(){
        try{
            routerURL = getRouterControlURL();
        }catch(Exception e){
            this.OutToJS(e.toString());
        }
    }

    public void paint(Graphics g) {
        g.drawString("Hello 6 " + routerURL, 20, 20);
    }

    public String getRouterControlURL() throws Exception
    {
        this.OutToJS("start22");
        UDPController udpClient = new UDPController();
        String location = udpClient.discoverRouterLocation();
        SOAPController soapClient = new SOAPController(location);
        return soapClient.getRouterURL();
    }

    public void OutToJS(String s)
    {
        System.out.println(s);
        /*
        JSObject win = JSObject.getWindow((Applet)this);
        Object[] arg = new Object[1];
        arg[0] = s;
        win.call("logFromJava", arg);
        */
    }
}