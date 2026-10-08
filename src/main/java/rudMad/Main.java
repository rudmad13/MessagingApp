package rudMad;

import rudMad.Server.Server;
import rudMad.Server.ServerStartup;
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
        Scanner input = new Scanner(System.in);
        
        ServerStartup.start(input).start();

    }


    private static void startClient() {
        Scanner input = new Scanner(System.in);

        try{
            ClientStartup.start(input).start();
        } catch (IOException e){
            System.err.println("Lost connection to server during setup!");
        }
        
    } 
    
}
