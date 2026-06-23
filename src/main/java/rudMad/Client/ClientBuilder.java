package rudMad.Client;

/**
 * This class is responsible for building a Client object.
 * Handling the creation of the Client object and the connection to the server.
 * It also handles the handshake with the server to validate the username.
 * If the handshake is rejected, the connection is closed and an exception is thrown.
 */

import java.util.Scanner;

import java.io.IOException;

import rudMad.Protocol.ClientHandshake;

public class ClientBuilder {

    private String host;
    private int port;
    private String username;
    private Scanner scanner;

    public ClientBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public ClientBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public ClientBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public ClientBuilder setScanner(Scanner scanner) {
        this.scanner = scanner;
        return this;
    }

    /**
     * This method is responsible for building and starting the client.
     * It creates a ClientConnection object to connect to the server.
     * It then performs a handshake with the server to validate the username.
     * If the handshake is rejected, the connection is closed and an exception is thrown.
     * If the handshake is accepted, a Client object is created and started.
     * 
     * @return Client object if the handshake is accepted, null otherwise.
     * @throws RuntimeException if the handshake is rejected or if there
     * is an error while connecting to the server.
     */
    public Client buildAndStart() {

        Client client = null;
        ClientConnection connection = null;

        try{
            
            connection = new ClientConnection(host, port);
            
            //Perform handshake with the server (validate username)
            if (! new ClientHandshake(connection.getOut(), connection.getIn(), username).handshake()){
                connection.close();
                throw new IOException("Handshake rejected by server");
            }

            client = new Client(connection, username, scanner);
            client.start();
            return client;
        } catch (IOException e) {
            if(connection != null){
                try{
                    connection.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException("Failed to build and start client ");
        }
    }
}
