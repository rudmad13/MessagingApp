package rudMad.Protocol;

import java.io.IOException;

import java.io.BufferedReader;
import rudMad.Server.ClientHandler;
import java.util.HashMap;

public class ServerHandshake implements HandShakeProtocol{

    

    private ClientHandler client;
    private HashMap<String,ClientHandler> clientList;

    public ServerHandshake(ClientHandler client, HashMap<String, ClientHandler> clientList){
        this.client = client;
        this.clientList = clientList;
    }

    @Override
    public boolean handshake() throws IOException {
        
        // Read the username
        BufferedReader reader = client.getInput();

        String username = reader.readLine();

        //Check for username duplicates
        if(clientList.containsKey(username)){
            client.sendMessage("REJECTED");
            client.closeConnection();
            return false;
        }else{
            client.setUsername(username);
            clientList.put(username, client);
            Thread thread = new Thread(client);
            thread.start();
            client.sendMessage("ACCEPTED");
            System.out.println(username + "has connected to the server");
            return true;


        }

    }


}
