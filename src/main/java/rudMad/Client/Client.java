package rudMad.Client;


import java.util.Scanner;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

import java.net.Socket;

public class Client {

    private Socket client;
    Scanner scanner;


    public void start(int port){

        scanner = new Scanner(System.in);
        try{

        client = new Socket("LocalHost", port);

        System.out.println("connection is successful");

        BufferedWriter out = new BufferedWriter( new OutputStreamWriter(client.getOutputStream()));
        
        String message = "";
        while(!(message.equals("quit()"))){

            System.out.println(">");
            message = scanner.nextLine();
            out.write(message);
            out.newLine();
            out.flush();



        }

        client.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
}
