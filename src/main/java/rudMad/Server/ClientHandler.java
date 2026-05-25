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

    public void run(){

        receiveMessage();
        
    }


    public void receiveMessage(){ // Send to the server
        try{
            String message;

            while((message = in.readLine()) != null){
                server.broadcast(message, this);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }


    public void sendMessage(String message){//Send to the client. Server -> client

        try{
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
        }

    }




}