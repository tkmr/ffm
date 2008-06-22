package ffm.js;
import netscape.javascript.*;
import java.applet.*;
import ffm.*;

public class JSCallback implements ICallback{
    private String jsfunc;
    private Applet applet;

    public JSCallback(String jsfunc, Applet applet){
        this.jsfunc = jsfunc;
        this.applet = applet;
    }

    public String call(String result){
        return this.call(result, this.jsfunc);
    }

    public String call(String result, String func){
        JSObject win = JSObject.getWindow(this.applet);
        Object[] arg = new Object[1];
        arg[0] = result;
        Object jsResult = win.call(func, arg);
        if(jsResult != null){
            return (String) jsResult;
        }else{
            return "";
        }
    }
}