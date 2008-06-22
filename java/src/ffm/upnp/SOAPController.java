package ffm.upnp;
import java.net.*;
import java.io.*;
import java.util.regex.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.rmi.RemoteException;
import javax.xml.namespace.QName;
import javax.xml.rpc.*;
import javax.xml.rpc.handler.*;
//import javax.xml.soap.SOAPBody;
//import javax.xml.soap.SOAPEnvelope;
//import javax.xml.soap.SOAPMessage;

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

    public String getExternalIP() throws IOException, ProtocolException, MalformedURLException
    {
        /*
          //javax.xml.rpc 関連のクラスを探すのが面倒なので
        Service soapService = ServiceFactory.newInstance().createService(new QName(SSDP_ST_SERVICE, "GetExternalIPAddress"));
        Call call = soapService.createCall();
        call.setOperationName(new QName(SSDP_ST_SERVICE, "GetExternalIPAddress"));
        call.setProperty(Call.OPERATION_STYLE_PROPERTY, "rpc");
        call.setProperty(Call.ENCODINGSTYLE_URI_PROPERTY, "http://schemas.xmlsoap.org/soap/encoding/");
        call.setTargetEndpointAddress(routerURL);
        Object[] args = {};
        String ystem.out.println(call.invoke(args));
        */

        String soapHeader = SSDP_ST_SERVICE + "#GetExternalIPAddress Host: " + InetAddress.getLocalHost().getHostAddress().toString();
        String postMessage = "<?xml version=\"1.0\"?>" +
            "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
            "<SOAP-ENV:Body><m:GetExternalIPAddress xmlns:m=\"urn:schemas-upnp-org:service:WANPPPConnection:1\"></m:GetExternalIPAddress></SOAP-ENV:Body>" +
            "</SOAP-ENV:Envelope>";

        System.out.println(routerURL);
        URL reqUrl = new URL(routerURL);
        HttpURLConnection con = (HttpURLConnection)reqUrl.openConnection();
        String result;

        try{
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);

            byte[] bytes = postMessage.getBytes();
            con.addRequestProperty("SoapAction", soapHeader);
            con.addRequestProperty("Content-Length", ""+bytes.length);

            //BufferedWriter output = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            //output.write(postMessage);
            //output.flush();

            Util.writeChunked(con.getOutputStream(), bytes);
            con.getOutputStream().close();

            con.connect();

            System.out.println(soapHeader);
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            System.out.println(soapHeader);

            String tmpLine;
            StringBuilder body = new StringBuilder();
            while((tmpLine = reader.readLine()) != null){
                body.append(tmpLine);
            }
            result = body.toString();
            reader.close();
        }finally{
            con.disconnect();
        }

        System.out.println(result);
        return result;
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