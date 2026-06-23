package rudMad.Protocol;

/**
 * This class is responsible for handling the handshake protocol between the client and the server.
 * It sends the username to the server and waits for a response.
 * If the server accepts the username, it returns true, otherwise false.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ClientHandshake implements HandShakeProtocol {

    private BufferedWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandshake(BufferedWriter out, BufferedReader in, String username) {
        this.out = out;
        this.in = in;
        this.username = username;
    }


    @Override
    public boolean handshake() {

        //Send username to server

        sendMessage(username);

        //Servers response to the username. If its accepted then return true, otherwise false.
        return usernameAccepted();
    }

    
    private boolean usernameAccepted() {
        try {
            String response = in.readLine();
            if (response.equals("ACCEPTED")) {
                return true;
            } else if (response.equals("REJECTED")) {
                return false;
            }
        } catch (IOException e) {
            System.out.println("Connection has been closed!");
            e.printStackTrace();
        }

        return false;
    }

        
    private void sendMessage(String message) {

        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
