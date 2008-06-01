import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

/*
  <APPLET
   CODE = udp2.class
   WIDTH = 600
   HEIGHT = 400 >
  </APPLET>
*/

public class udp2 extends Applet implements ActionListener
{
    TextArea textarea1;
    Button button1;

    public void init()
    {
        textarea1 = new TextArea("メッセージ", 3, 60, TextArea.SCROLLBARS_BOTH);
        add(textarea1);
        button1 = new Button("送信");
        add(button1);
        button1.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==button1){
            try{
                // UDPパケットを送信する先となるブロードキャストアドレス (12345番ポート)
                InetSocketAddress remoteAddress = new InetSocketAddress("239.255.255.250", 1900);

                // UDPパケットに含めるデータ
                byte[] sendBuffer =
                    "M-SEARCH * HTTP/1.1\r\n"+
                    "MX: 3\r\n"+
                    "HOST: 239.255.255.250:1900\r\n"+
                    "MAN: \"ssdp:discover\"\r\n"
                    "ST: urn:schemas-upnp-org:service:WANPPPConnection:1\r\n\r\n".getBytes("UTF-8");

                byte[] sendBuffer2 = new byte[sendBuffer.length-6];
                for(int i=3,j=0; i<sendBuffer.length-3; i++,j++){
                    sendBuffer2[j] = sendBuffer[i];
                }

                // UDPパケット
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer2, sendBuffer2.length, remoteAddress);

                // DatagramSocketインスタンスを生成して、UDPパケットを送信
                new DatagramSocket().send(sendPacket);
            }
            catch(Exception ee){
                textarea1.replaceRange(ee.getMessage(),
                                       textarea1.getSelectionStart(),
                                       textarea1.getSelectionEnd() );
            }
        }
    }
}