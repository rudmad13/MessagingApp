package rudMad.Client;

import java.io.IOException;
import java.net.ProtocolException;

import rudMad.Protocol.ClientHandshake;

/** Completes TLS and username admission before returning a usable connection. */
public class ClientConnectionFactory {

    /**
     * Responsible for the creation of the ClientConnection object.
     * @param host - IP address of server
     * @param username  - Username of client
     * @param port - Port the server is listening on
     * @return ClientConnection object
     * @throws IOException - Problems occured connection not established
     * @throws ProtocolException - Username was taken!
     */
    public static ClientConnection connect(String host, String username, int port) throws IOException, ProtocolException {

        //Initial connection to the server
        ClientConnection connection = new ClientConnection(host, port);
        //Start handshake
        new ClientHandshake(connection.getOut(), connection.getIn(), username);
        connection.getSocket().setSoTimeout(0);
        return connection;
    }
}
 