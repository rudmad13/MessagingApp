package rudMad.Client;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private Socket client;


    public void start(int port){


        try{
        client = new Socket("LocalHost", port);

        System.out.println("connection is successful");

        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
}
