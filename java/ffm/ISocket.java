package ffm;
import java.net.*;
import java.io.*;

public interface ISocket{
    void connect(String host, int timeout) throws IOException, SocketException;
    void write(String request) throws IOException;
    void write(String[] requests) throws IOException;
    String read() throws IOException;
    void close() throws IOException;
}