package ffm;
import java.net.*;
import java.io.*;

public interface ISocket{
    void connect(String host, int timeout) throws IOException, SocketException;
    void write(String request) throws IOException;
    void write(String[] requests) throws IOException;
    void close() throws IOException;
    ISocket generate(Object rawsocket) throws IOException;
    String readLine() throws IOException;
    String readAll() throws IOException;
    char read() throws IOException;
    String read(int length) throws IOException;
}