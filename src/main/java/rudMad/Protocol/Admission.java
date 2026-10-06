package rudMad.Protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.HashMap;

import rudMad.Server.ClientHandler;

public class Admission {



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



    public static void serverAdmission(ClientHandler client,  HashMap<String, ClientHandler> clientList) throws IOException{

        BufferedReader in = client.getInput();

        String username = in.readLine();

        if(clientList.containsKey(username)){
            client.sendMessage(AdmissionResponse.REJECTED.name());

        }else {
            client.setUsername(username);
            clientList.put(username, client);
            Thread thread = new Thread(client);
            thread.start();
            client.sendMessage(AdmissionResponse.ACCEPTED.name());
            System.out.println(username + " has connected to the server");

        }



    }

}
