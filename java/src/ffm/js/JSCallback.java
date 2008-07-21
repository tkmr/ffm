package ffm.js;
import netscape.javascript.*;
import java.util.List;
import java.util.ArrayList;
import java.applet.*;
import ffm.*;

public class JSCallback<T> implements ICallback<T>{
    private String jsfunc;
    private Applet applet;

    public JSCallback(String jsfunc, Applet applet){
        this.jsfunc = jsfunc;
        this.applet = applet;
    }

    public String call(T request){
        System.out.println(this.jsfunc);
        JSObject win = JSObject.getWindow(this.applet);
        Object[] arg = new Object[1];
        arg[0] = request;
        Object jsResult = win.call(this.jsfunc, arg);
        if(jsResult != null){
            return (String) jsResult;
        }else{
            return "";
        }
    }
}