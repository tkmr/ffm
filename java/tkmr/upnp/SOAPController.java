package tkmr.upnp;
import java.net.*;
import java.io.*;
import java.util.regex.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class SOAPController{
    private final String SSDP_ST_SERVICE   = "urn:schemas-upnp-org:service:WANPPPConnection:1";
    private final String SSDP_ST_IPSERVICE = "urn:schemas-upnp-org:service:WANIPConnection:1";

    private String routerURL;
    public String getRouterURL(){
        return routerURL;
    }

    public SOAPController(String routerLocation) throws MalformedURLException, XPathExpressionException, IOException, SAXException
    {
        routerURL = discoverControlURL(routerLocation);
    }



    public String discoverControlURL(String location) throws XPathExpressionException, MalformedURLException, IOException, SAXException
    {
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

        if(parent == null){
            return null;
        }

        URL controlURL;
        String controlUrlPath = parent.getElementsByTagName("controlURL").item(0).getTextContent();
        controlURL = new URL(ssdpUrl.getProtocol(), ssdpUrl.getHost(), ssdpUrl.getPort(), controlUrlPath);
        return controlURL.toString();
    }
}