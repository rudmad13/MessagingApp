package rudMad.Client;

import java.util.Scanner;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;

import java.net.Socket;
import java.net.UnknownHostException;


public class Client {

    private Socket client;
    private Scanner scanner;
    private BufferedWriter out;// To Server
    private BufferedReader in;// From Server
    private ServerListener listener;
    private String username;

    public Client(String ip_address, int port, String username) {
        try {

            client = connect(ip_address, port);
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.listener = new ServerListener(in);
            this.scanner = new Scanner(System.in);
            this.username = username;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method is responsible for starting the client.
     * It starts a thread to listen for incoming messages from the server
     * Another thread is started to listen for user input from the keyboard and send
     * it to the server
     * If the user types "quit()", the client will disconnect from the server and
     * close the connection
     */
    public void start() {

        if (client != null) {

            if (handShake(this.username)) {

                System.out.println("Successfully connected to the server!");

                Thread listener = new Thread(this.listener);

                listener.start();

                Thread kb = new Thread(() -> {

                    String message = "";
                    while (!(message.equals("quit()"))) {
                        System.out.print("~ ");
                        message = scanner.nextLine();
                        sendMessage(out, message);
                    }

                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                kb.start();
            } else {
                try{
                    client.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
                System.out.println("Username is already taken. Please try again with a different username.");
            }
        } else {
            System.out.println("Failure to connect to server");
        }
    }

    /**
     * This method is responsible for the handshake process between the client and
     * the server
     * Client sends the username to the server.
     * If the server receives a username that is not valid, it will close the
     * connection with the client.
     * Otherwise, the client will be added to the server's client list.
     * 
     * @param username - Representing the username that the client wants to use on
     *                 the server
     * @return True if the handshake process is successful, false otherwise
     */
    private boolean handShake(String username) {

        // Send username to the server
        sendMessage(out, username);
        // Check to see if the connection is open.
        // If its closed then the return value is false, otherwise true.
        if (usernameAccepted()) {
            this.username = username;
            return true;
        }
        return false;
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

    private Socket connect(String ip_address, int port) {
        try {

            this.client = new Socket(ip_address, port);

            return client;

        } catch (UnknownHostException e) {
            System.out.println("Host cannot be found");
            e.printStackTrace();
        } catch (IllegalArgumentException ill) {
            System.out.println("port value must be within 0 and 65535");
            ill.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }

        return null;
    }

    private void sendMessage(BufferedWriter out, String message) {

        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
