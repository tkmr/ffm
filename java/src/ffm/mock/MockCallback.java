package ffm.mock;
import java.io.*;
import java.util.HashMap;
import ffm.*;

public class MockCallback<T> implements ICallback<T>{
    private String key;
    public MockCallback(){
        this.key = "default";
    }
    public MockCallback(String key){
        this.key = key;
    }

    public String call(T request)
    {
        String result;
        if(this.key.equals("echoToSocket")){
            result = echoToSocket((ISocket)request);
        }else if(this.key.equals("echoNabeatsu")){
            result = echoNabeatsu((Integer)request);
        }else{
            result = "default";
        }
        return result;
    }

    private String echoToSocket(ISocket socket)
    {
        String request = "";
        try{
            request = socket.readLine();
            socket.write("request is " + request + "\r\n");
        }catch(IOException e){
            System.out.println(e.toString());
        }
        return request;
    }

    private String echoNabeatsu(Integer request)
    {
        return ((request % 3) == 0 && request != 0) ? null : request.toString();
    }
}