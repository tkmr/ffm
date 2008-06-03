package tkmr;
import java.net.*;
import java.io.*;
import java.util.regex.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class UPnPUtil
{
    public static String getLocation(String ssdpMessage)
    {
        Pattern p = Pattern.compile("LOCATION: (.*)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(ssdpMessage);
        m.find();
        return m.group(1);
    }

    public static Document getXMLHttp(String url) throws IOException, ProtocolException, MalformedURLException, SAXException, ParserConfigurationException
    {
        //String body = UPnPUtil.getHTTP(url);
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
        Document doc = domBuilder.parse(url);
        return doc;
    }

    public static String getHTTP(String url) throws IOException, ProtocolException, MalformedURLException
    {
        URL reqUrl = new URL(url);
        HttpURLConnection urlcon = (HttpURLConnection)reqUrl.openConnection();
        urlcon.setRequestMethod("GET");
        urlcon.connect();
        //urlcon.getResponseCode();
        //urlcon.getResponseMessage();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));
        String tmpLine;
        StringBuilder body = new StringBuilder();
        while((tmpLine = reader.readLine()) != null){
            body.append(tmpLine);
        }
        reader.close();
        urlcon.disconnect();
        return body.toString();
    }
}