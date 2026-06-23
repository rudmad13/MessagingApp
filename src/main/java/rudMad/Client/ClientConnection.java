package rudMad.Client;

/**
 * This class is responsible for handling the connection to the server.
 * It creates a socket to connect to the server and provides methods to get the input
 * and output streams of the socket.
 * It also provides a method to close the connection.
 */
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;

public class ClientConnection {
    private final Socket socket;
    private final BufferedWriter out;
    private final BufferedReader in;

    public ClientConnection(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public Socket getSocket() {
        return socket;
    }
    public BufferedWriter getOut() {
        return out;
    }
    public BufferedReader getIn() {
        return in;
    }

    public void close() throws IOException {
        socket.close();
    }

}
