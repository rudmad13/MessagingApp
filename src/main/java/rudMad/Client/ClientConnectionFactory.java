package rudMad.Client;

import java.io.IOException;
import rudMad.Protocol.ClientHandshake;

/** Completes TLS and username admission before returning a usable connection. */
public class ClientConnectionFactory {
    public static ClientConnection connect(String host, String username, int port) throws IOException {
        ClientConnection connection = new ClientConnection(host, port);
        try {
            if (!new ClientHandshake(connection.getOut(), connection.getIn(), username).handshake()) {
                throw new IOException("Username is taken! Try again.");
            }
            connection.getSocket().setSoTimeout(0);
            return connection;
        } catch (IOException | RuntimeException e) {
            try {
                connection.close();
            } catch (IOException closeError) {
                e.addSuppressed(closeError);
            }
            throw e;
        }
    }
}
