package rudMad.Server;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;


public class Server {

    private ArrayList<ClientHandler> clientList;
    private ServerSocket server;

    public Server(int port){
        this.clientList = new ArrayList<>();

        try{
            this.server = new ServerSocket();

        } catch (IllegalArgumentException error){
            System.out.println("Port value must be between 0 65553, inclusive");
            error.printStackTrace();

        }catch (IOException io){
            System.out.println("Error when opening the socket");
            io.printStackTrace();
        }
    }
    


    public void start(){

        boolean power = true;

        System.out.println("Server is listening on port " + server.getLocalPort());

        while(power){

            try {
                Socket client = server.accept();

                ClientHandler newClient = new ClientHandler(client, this);

                clientList.add(newClient);

                Thread thread = new Thread(newClient);
                thread.start();

            } catch (IOException e){
                e.printStackTrace();
            }

        }

    }

    public void broadcast(String message, ClientHandler handler){

        for(ClientHandler client : clientList){

            if (client != handler){
                continue;
            }

            client.sendMessage(message);
        }

    }
    
}
