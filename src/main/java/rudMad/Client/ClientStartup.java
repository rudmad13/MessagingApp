package rudMad.Client;

import java.io.IOException;
import java.net.ProtocolException;
import java.security.GeneralSecurityException;
import java.util.Scanner;

import rudMad.Protocol.Admission;
import rudMad.Protocol.ClientHandshake;

public class ClientStartup {

    
    public static Client start(Scanner kb) throws IOException{

        while(true){
            try{
                //Prompt for truststore information
                System.out.println("Enter absolute path for truststore: ");
                String path = kb.nextLine();
                System.out.println("Enter password for truststore: ");
                String password = kb.nextLine();
                //Validate 
                TlsConfig.configureTrustStore(path, password);
                break;
            } catch (GeneralSecurityException e){
                System.err.println(e.getMessage());
            }catch (IOException i){
                System.err.println(i.getMessage());
            }
        }

        TlsConnection connection = null;
        String username = null;
        while (connection == null && username == null){
            System.out.println("Enter ip address for server: ");
            String ip = kb.nextLine();
            System.out.println("Enter the port the user is entering: ");
            int port = kb.nextInt();
            System.out.println("Enter a username: ");
            String temp = kb.nextLine();

            try{
                connection = new TlsConnection(ip, port);
                Admission.clientAdmission(connection.getIn(), connection.getOut(), temp);
                username = temp;
            }catch (ProtocolException i){
                System.err.println("Username is taken!");
            } catch (IOException e){
                System.err.println("Unable to connect try again: " + e.getMessage());
            }
        }


        return new Client(connection, username);

    }

}
