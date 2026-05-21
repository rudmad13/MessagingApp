package rudMad.Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {


    public void start(int port){
        
        try{

        ServerSocket server = new ServerSocket(port);
        System.out.println("Server is listening on"+ server.getLocalPort());

        Socket client = server.accept();
        System.out.println("Connection made" + client.getInetAddress());


        server.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
