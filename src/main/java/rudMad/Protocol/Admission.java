package rudMad.Protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.logging.Logger;

import rudMad.Server.ClientHandler;
import rudMad.Server.Server;

public class Admission {


    private static final Logger logger = Logger.getLogger(Server.class.getName());



    /**
     * Sends request to server to check if username is valid
     * @throws IOException - Errors occures during reading and writing in streams
     * @throws ProtocolException - Username is taken
     */
    public static void clientAdmission(BufferedReader in , BufferedWriter out, String username) throws IOException, ProtocolException{
        
        out.write(username);
        out.newLine();
        out.flush(); 

        AdmissionResponse response = AdmissionResponse.valueOf(in.readLine());


        if (response == AdmissionResponse.REJECTED){
            throw new ProtocolException("Username is taken!");

        }
    }


    /**
     * Checks for duplicate usernames in the server. 
     * Rejected if username is taken.
     * Accepted if username is not taken. Added to the clientlist of the server
     * @throws IOException Cannot read from input. Connection might be lost
     */
    public static void serverAdmission(ClientHandler client,  HashMap<String, ClientHandler> clientList) throws IOException{

        BufferedReader in = client.getInput();

        String username = in.readLine();

        if(clientList.containsKey(username)){
            client.sendMessage(AdmissionResponse.REJECTED.name());
            client.closeConnection();
            logger.info("Connection failed due to duplicate name: " + username);

        }else {
            client.setUsername(username);
            clientList.put(username, client);
            Thread thread = new Thread(client);
            thread.start();
            client.sendMessage(AdmissionResponse.ACCEPTED.name());
            logger.info(username + " has connected to the server");

        }



    }

}
