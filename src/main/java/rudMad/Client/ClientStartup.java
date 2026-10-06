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
        while (connection == null){
            System.out.println("Enter ip address for server: ");
            String ip = kb.nextLine();
            System.out.println("Enter the port the user is entering: ");
            int port = kb.nextInt();

            try{
                connection = new TlsConnection(ip, port);
            }catch (IOException e){
                System.err.println("Unable to connect try again: " + e.getMessage());
            }
        }

        String username = null;
        while (username == null){
            System.out.println("Enter a username: ");
            String temp = kb.nextLine();
            
            try{

                Admission.clientAdmission(connection.getIn(), connection.getOut(), temp);

            }catch (ProtocolException e){
                System.err.println("Username taken!" + e.getMessage());
            } catch (IOException i){
                throw i;

            }
        }

        return new Client(connection, username);

    }

}
