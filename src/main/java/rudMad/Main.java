package rudMad;

import rudMad.Server.Server;
import rudMad.Client.Client;
import rudMad.Client.ClientConnection;
import rudMad.Client.ClientConnectionFactory;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0){
            System.out.println("Usage: -s or -c");

            return;
        }

        switch (args[0]){
            case "-s":
                startServer(Integer.parseInt(args[1]));
                break;

            case "-c":
                startClient(args[1], Integer.parseInt(args[2]), args[3]);
                break;
            
            default:
                System.out.println("Unknown argument");
        }

        
    }


    private static void startServer(int port){

        Server server = new Server(port);

        server.start();


    }


    private static void startClient(String ip_address, int port, String username){

       try{
        ClientConnection connection =  ClientConnectionFactory.connect(ip_address, username, port);
        new Client(connection, username);
       }catch (IOException e){
        System.out.println("Unable to connect to server");
       }
 
    } 
    
}
