package rudMad;

import rudMad.Server.Server;
import rudMad.Client.ClientStartup;

import java.io.IOException;
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
                startClient();
                break;
            
            default:
                System.out.println("Unknown argument");
        }

        
    }


    private static void startServer(int port){

        try {
            Server server = new Server(port);
            server.start();
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Unable to start TLS server: " + e.getMessage());
        }


    }


    private static void startClient() {
        Scanner input = new Scanner(System.in);
        ClientStartup.start(input).start();
    } 
    
}
