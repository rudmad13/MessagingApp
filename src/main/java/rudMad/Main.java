package rudMad;

import rudMad.Server.Server;
import rudMad.Client.Client;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {

        if (args.length == 0){
            System.out.println("Usage: -s or -c");

            return;
        }

        switch (args[0]){
            case "-s":
                startServer();
                break;

            case "-c":
                startClient();
                break;
            
            default:
                System.out.println("Unknown argument");
        }

        
    }


    private static void startServer(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a port to open: ");

        int port = scanner.nextInt();
        scanner.close();

        Server server = new Server(port);

        server.start();


    }


    private static void startClient(){

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the port you want to connect to: ");

        int port = scanner.nextInt();
        scanner.close(); 

        Client client = new Client();

        client.start(port);

    }
    
}
