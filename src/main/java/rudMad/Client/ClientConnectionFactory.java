package rudMad.Client;

import java.io.IOException;


import rudMad.Protocol.ClientHandshake;

public class ClientConnectionFactory {

    public static ClientConnection connect(String host, String username, int port) throws IOException{
        
        ClientConnection connection = new ClientConnection(host, port);
        
        //Perform handshake
        if(!new ClientHandshake(connection.getOut(), connection.getIn(),  username).handshake()){
            connection.close();
            System.out.println("Username is taken! Try again");
            throw new IOException("Username is taken !");
        }
        return connection;
        
    }


}
