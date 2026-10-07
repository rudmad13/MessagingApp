package rudMad.Client;

import java.net.Socket;
import java.net.InetSocketAddress;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/** Opens a verified TLS connection and exposes its application streams. */
public class TlsConnection {
    private static final int SETUP_TIMEOUT_MS = 10_000;
    private final SSLSocket socket;
    private final BufferedWriter out;
    private final BufferedReader in;


    /**
     * Creating a Socket and connection to the server
     * @throws - IOException. Closes possible server connection
     */
    public TlsConnection(String host, int port) throws IOException {
        
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        this.socket = (SSLSocket) factory.createSocket();

        //Configure socket
        configureSocket(socket);
        
        try {
            socket.connect(new InetSocketAddress(host, port), SETUP_TIMEOUT_MS);
            socket.setSoTimeout(SETUP_TIMEOUT_MS);
            socket.startHandshake();
            socket.setSoTimeout(0);
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {// If something fails during creation cleanup and rethrow original exception
            try {
                socket.close();
            } catch (IOException closeError) {
                e.addSuppressed(closeError);
            }
            throw e;
        }
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


    private void configureSocket(SSLSocket socket){

        socket.setEnabledProtocols(new String[] {"TLSv1.3"});
        SSLParameters parameters = socket.getSSLParameters(); 
        parameters.setEndpointIdentificationAlgorithm("HTTPS");
        socket.setSSLParameters(parameters);
    }
}
