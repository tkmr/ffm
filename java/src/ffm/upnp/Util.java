package ffm.upnp;
import java.net.*;
import java.io.*;
import java.util.regex.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Util
{
    public static Document getXMLHttp(String url) throws IOException, ProtocolException, MalformedURLException, SAXException
    {
        //String body = UPnPUtil.getHTTP(url);
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder domBuilder;
        Document doc;
        try{
            domBuilder = domFactory.newDocumentBuilder();
            doc = domBuilder.parse(url);
        }catch(ParserConfigurationException e){
            return null;
        }
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

    private static final int chunkSize = 1000;
    public static void writeChunked(OutputStream os, byte [] bytes) throws IOException {
        int off = 0;
        while (off < bytes.length){
            int len = Math.min(chunkSize, bytes.length-off);
            os.write(bytes, off, len);
            off += len;
            os.flush();
        }
    }
}