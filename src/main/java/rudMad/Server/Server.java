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
            this.server = new ServerSocket(port);

        } catch (IllegalArgumentException error){
            System.out.println("Port value must be between 0-65553, inclusive");
            error.printStackTrace();

        }catch (IOException io){
            System.out.println("Error when opening the socket");
            io.printStackTrace();
        }
    }
    


    public void start(){

        boolean power = true;

        System.out.println("Server is listening on port " + server.getLocalPort());
        
        int i = 0;
        while(power){

            try {
                Socket client = server.accept();

                ClientHandler newClient = new ClientHandler(client, this);

                clientList.add(newClient);
                System.out.println("New Connection was made: " + i + 1);

                Thread thread = new Thread(newClient);
                thread.start();

            } catch (IOException e){
                e.printStackTrace();
            }

        }

    }

    public void broadcast(String message, ClientHandler handler){

        System.out.println("Broadcasting: " + message);

        for(ClientHandler client : clientList){

            if (client == handler){
                continue;
            }

            client.sendMessage(message);
        }

    }
    
}
