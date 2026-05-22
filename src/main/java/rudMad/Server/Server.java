package rudMad.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.ServerSocket;
import java.net.Socket;
public class Server {


    public void start(int port){
        
        try{

        ServerSocket server = new ServerSocket(port);
        System.out.println("Server is listening on "+ server.getLocalPort());

        Socket client = server.accept();
        System.out.println("Connection made " + client.getInetAddress());

        //Take in the Input and Output buffer
        BufferedReader in =  new BufferedReader(new InputStreamReader(client.getInputStream()));

        String message;
        //Keep reading while the connection is open.
        while((message = in.readLine()) != null){

            System.out.println(message);

        }

        server.close();

        System.out.println("Connection is closed");

        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
