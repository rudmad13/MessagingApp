/**
 * This class represents the server
 * It is responsible for accepting/validating connections.
 */

package rudMad.Server;

import java.io.IOException;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;


import java.util.HashMap;

import rudMad.Protocol.Admission;
import rudMad.Protocol.ServerHandshake;




public class Server {

    private HashMap<String,ClientHandler> clientList; 
    private final SSLServerSocket server;

    public Server(int port) throws IOException {
        this.clientList = new HashMap<String, ClientHandler>();
        if (System.getProperty("javax.net.ssl.keyStore") == null) {
            throw new IOException("Set javax.net.ssl.keyStore to a keystore containing the server certificate and private key.");
        }
        SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        this.server = (SSLServerSocket) factory.createServerSocket(port);
        this.server.setEnabledProtocols(new String[] {"TLSv1.3"});
        this.server.setNeedClientAuth(false);
    }
    /**
     * This method is a loop. The server accepts a connection. Checks for 
     * username uniqueness. Re-Enter the loop, looking for another connection
     */
    public void start(){

        boolean running = true;

        System.out.println("TLS server is listening on port " + server.getLocalPort());
        
        while(running){

            SSLSocket client = null;
            try {
                client = (SSLSocket) server.accept();
                client.setSoTimeout(10_000);
                client.startHandshake();
                client.setSoTimeout(0);

                ClientHandler newClient = new ClientHandler(client, this);

                Admission.serverAdmission(newClient, clientList);

            } catch (IOException e){
                if (client != null) {
                    try {
                        client.close();
                    } catch (IOException closeError) {
                        e.addSuppressed(closeError);
                    }
                }
                System.out.println("Connection Failed");
                e.printStackTrace();
            }

        }

    }

    /**
     * This method is responsible of broadcasting a clients message to everyone else on the server
     * @param message - Message that will be broadcasted
     * @param handler - Representing the handler responsible for the client sending the message
     */
    public void broadcast(String message, ClientHandler handler){

        //Debug purposes: System.out.println(handler.getUsername() + ": " + message);

        for(ClientHandler client : clientList.values()){

            if (client == handler){
                continue;
            }

            client.sendMessage(handler.getUsername() + ": " + message);
        }

    }

    /**
     * This method is responsible for username duplicates.
     * @param username - Representing the username to be checked
     * @return boolean - True if it exists, otherwise false
     */
    public boolean existsUsername(String userName){
        return clientList.containsKey(userName);
    }

    /**
     * This method is responsible for removing a client from the server
     * @param username - Representing the username of the client to be removed
     */
    public void removeClient(String username){
        clientList.remove(username);
        System.out.println(username + " has disconnected from the server!");
    }
    
}
