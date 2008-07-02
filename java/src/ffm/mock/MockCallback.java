package ffm.mock;
import java.util.HashMap;
import ffm.*;

public class MockCallback<T> implements ICallback{
    private HashMap<T, String> hash;

    public JSCallback(HashMap<T, String> hash){
        this.hash = hash;
    }

    public String call(T request, String key){
        return this.hadh.get(key);
    }
}