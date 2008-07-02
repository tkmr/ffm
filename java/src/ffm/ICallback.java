package ffm;
public interface ICallback<T>{
    String call(T args, String key);
}