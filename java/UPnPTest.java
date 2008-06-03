import tkmr.*;
import java.util.regex.*;
import java.net.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;

public class UPnPTest{
    public static void main(String[] args){
        try{
            UPnPController upnp = new UPnPController();

            DatagramPacket receivePacket = upnp.discoverRouter("239.255.255.250", 1900);
            String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String location = UPnPUtil.getLocation(message);
            //TODO: Null check;
            URL ssdpUrl = new URL(location);
            Document doc = UPnPUtil.getXMLHttp(ssdpUrl.toString());

            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            NodeList resultNodes = (NodeList)xpath.evaluate("//service/serviceType", doc, XPathConstants.NODESET);

            String content;
            Element parent = null;
            Pattern pattern = Pattern.compile(UPnPController.SSDP_ST_IPSERVICE+"|"+UPnPController.SSDP_ST_SERVICE);
            for(int i = 0; i < resultNodes.getLength(); i++){
                content = resultNodes.item(i).getTextContent();
                if(pattern.matcher(content).find()){
                    parent = (Element)resultNodes.item(i).getParentNode();
                }
            }
            if(parent != null){
                String controlUrlPath = parent.getElementsByTagName("controlURL").item(0).getTextContent();
                URL controlURL = new URL(ssdpUrl.getProtocol(), ssdpUrl.getHost(), ssdpUrl.getPort(), controlUrlPath);
                System.out.println(controlURL);
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
}