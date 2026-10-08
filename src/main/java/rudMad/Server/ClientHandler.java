 /**
  * The client handler class is responsible for handling the connection and communication
  * between the server and a single client. 1 Thread is created for each clinet connection.
  */


package rudMad.Server;

import java.net.Socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ClientHandler implements Runnable{

    private Socket client; //Server endpoint for this client connection
    private Server server;
    private BufferedReader in; //Data coming into the server from this client
    private BufferedWriter out; //Data going out from the server to this client
    private String username;//Unique name on the server

    public ClientHandler(Socket client, Server server){
        this.client = client;
        this.server = server;

        try{
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public BufferedReader getInput(){
        return this.in;
    }

    public BufferedWriter getOutput(){
        return this.out;
    }

    public void closeConnection() throws IOException{
        client.close();
    }

    @Override
    public void run() {

        receiveMessage();
        
    }


    /**
     * Waits to receieve messages from the client
     * Reads input and broadscasts input to the rest of the connection on the server
     * @throws IOException Error network I/O
     */
    public void receiveMessage(){ // Send to the server
        try{
            String message;

            while((message = in.readLine()) != null){
                server.broadcast(message, this);
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            server.removeClient(this);
        }

    }


    /**
     * Server sends 'message' to client
     * @param String message to write to client
     */
    public void sendMessage(String message)throws IOException{

            out.write(message);
            out.newLine();
            out.flush();

    }

}