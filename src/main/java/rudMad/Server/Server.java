/**
 * This class represents the server
 * It is responsible for accepting/validating connections.
 */

package rudMad.Server;

import java.io.IOException;
import java.net.SocketException;
import java.net.InetSocketAddress;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;


import java.util.HashMap;
import java.util.logging.Logger;

import rudMad.Protocol.Admission;
import rudMad.Server.VoiceChannel.ServerVoiceSession;
import rudMad.Server.VoiceChannel.VoiceRelay;
import rudMad.Config.LoggingConfig;



public class Server {

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private HashMap<String,ClientHandler> clientList; 
    private final SSLServerSocket server;

    private ServerVoiceSession voiceSession;    


    /**
     * 
     * @param port
     * @throws IOException Network Errors
     * @throws IllegalArgumentException Port must be between 0 and 65535 inclusive 
     */
    public Server(int port) throws IOException, IllegalArgumentException{
        this.clientList = new HashMap<String, ClientHandler>();
        SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        this.server = (SSLServerSocket) factory.createServerSocket(port);
        this.server.setEnabledProtocols(new String[] {"TLSv1.3"});
        this.server.setNeedClientAuth(true);  

        LoggingConfig.configureServerLogging();
    }
    

    /**
     * This class will create start and return the local port the voice session is working on
     * @return int - Representing the local port that is opened for the voice session
     */
    public synchronized int openVoiceSession() {
        if(voiceSession == null){
            try{
            voiceSession = new ServerVoiceSession(new VoiceRelay());
            voiceSession.start();
            } catch (IOException e){
                System.err.println("Unable to open port for voice session: " + e.getMessage());
            }
        }

        int port = voiceSession.getPort();

        System.out.println(port);

        return port;
    }

    /** Opens the shared relay before adding or updating the participant. */
    public synchronized int joinVoice(String username, InetSocketAddress udpAddress) throws SocketException {
        int serverPort = openVoiceSession();
        voiceSession.addParticipant(username, udpAddress);
        return serverPort;
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


        for(ClientHandler client : clientList.values()){

            if (client == handler){
                continue;
            }

            try{
            client.sendMessage(handler.getUsername() + ": " + message);
            } catch (IOException io){
                continue;
            }
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

    private synchronized void removeVoiceParticipant(String username) {
        if (voiceSession != null && username != null) {
            voiceSession.removeParticipant(username);
        }
    }

    /**
     * This method is responsible for removing a client from the server
     * @param username - Representing the username of the client to be removed
     */
    public void removeClient(ClientHandler client){
        try{
            client.closeConnection();

        }catch (IOException e){
        }

        removeVoiceParticipant(client.getUsername());
        clientList.remove(client.getUsername());
        logger.info( client.getUsername() + " has disconnected from the server!");
    }
    
}
