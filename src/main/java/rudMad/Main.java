package rudMad;

import rudMad.Server.Server;
import rudMad.Client.ClientBuilder;

import java.util.Scanner;

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

        new ClientBuilder()
            .setHost(ip_address)
            .setPort(port)
            .setUsername(username)
            .setScanner(new Scanner(System.in))
            .buildAndStart();

    }
    
}
