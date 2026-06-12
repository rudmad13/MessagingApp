/**
 * This class represents the server
 * It is responsible for accepting/validating connections.
 */

package rudMad.Server;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.HashMap;

import java.io.BufferedReader;


public class Server {

    private HashMap<String,ClientHandler> clientList; 
    private ServerSocket server;

    public Server(int port){

        this.clientList = new HashMap<String,ClientHandler>();

        try{
            this.server = new ServerSocket(port);

        } catch (IllegalArgumentException error){
            System.out.println("Port value must be between 0-65553, inclusive");
            error.printStackTrace();

        }catch (IOException io){
            System.out.println("Error when opening the socket");
            io.printStackTrace();
        }
    }
    


    /**
     * This method is a loop. The server accepts a connection. Checks for 
     * username uniqueness. Re-Enter the loop, looking for another connection
     */
    public void start(){

        boolean power = true;

        System.out.println("Server is listening on port " + server.getLocalPort());
        
        while(power){

            try {
                Socket client = server.accept();

                ClientHandler newClient = new ClientHandler(client, this);

                //Get the username
                BufferedReader reader = newClient.getInput();

                String username = reader.readLine();

                if(!(existsUsername(username))){
                    newClient.setUsername(username);
                    clientList.put(username, newClient);
                    Thread thread = new Thread(newClient);
                    thread.start();

                }else{
                    newClient.closeConnection();
                }

            } catch (IOException e){
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

            client.sendMessage(message);
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
    }
    
}
