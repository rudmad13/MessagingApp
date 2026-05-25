package rudMad.Client;

import java.io.BufferedReader;
import java.io.IOException;


public class ServerListener implements Runnable{

    private BufferedReader in;

    public ServerListener( BufferedReader in){
        this.in = in;
    }

    public void run(){

        String message;

        try{
            while((message = in.readLine()) != null){
                System.out.println(message);
            }

        } catch (IOException e){
            e.printStackTrace();
        }      



    }


    
}
