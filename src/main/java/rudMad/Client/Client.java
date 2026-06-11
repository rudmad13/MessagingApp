package rudMad.Client;


import java.util.Scanner;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;


import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private Socket client;
    private Scanner scanner;
    private BufferedWriter out;//To Server
    private BufferedReader in;//From Server
    private ServerListener listener;


    public Client(String ip_address, int port){
        try{

            client = connect(ip_address, port);
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.listener = new ServerListener(in);
            this.scanner = new Scanner(System.in);

            } catch (IOException e){
                e.printStackTrace();
            }

    }

    public void start(){


        if (client != null){

            Thread listener = new Thread(this.listener);

            listener.start();

            Thread kb = new Thread(() -> {

                String message = "";
                while(!(message.equals("quit()"))){
                    System.out.print("~ ");
                    message = scanner.nextLine();
                    sendMessage(out, message);
                }

                try{
                    client.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            });

            kb.start();
        }else{
            System.out.println("Failure to connect to server");
       }
    }

    private Socket connect(String ip_address, int port){
        try{

           this.client = new Socket(ip_address, port);

            return client;

        } catch (UnknownHostException e){
            System.out.println("Host cannot be found");
            e.printStackTrace();
        } catch (IllegalArgumentException ill){
            System.out.println("port value must be within 0 and 65535");
            ill.printStackTrace();
        } catch (IOException io){
            io.printStackTrace();
        }

        return null;
    }

    private void sendMessage(BufferedWriter out, String message){

        try{
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
}
