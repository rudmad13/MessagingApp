package rudMad.Server;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.util.Scanner;

import rudMad.Client.TlsConfig;

public class ServerStartup {

    public static Server start(Scanner kb){

        String path = null;
        String password = null;

        while(path == null && password == null){

            System.out.println("Enter absolute path for Keystore: ");
            String tempPath = kb.nextLine();
            System.out.println("Enter password for Keystore");
            String tempPass = kb.nextLine();

            try{
            TlsConfig.configureKeyStore(path, tempPass);

            path = tempPath;
            password = tempPass;

            } catch (IOException io){
                System.err.println("Unable to open/read file");

            } catch (KeyStoreException key){
                System.err.println("No Support for PKCS12 type");
            } catch (GeneralSecurityException gen){
                System.err.println("Integrity algorithm missing or certificate cannot be loaded");

            }
        }

        Server server = null;
        int port = -1;
        while ((port < 0 || port > 65535)  || server == null){

            System.out.println("Enter port number between 0 and 65535 ");

            port = Integer.parseInt(kb.nextLine());

            try{
                server = new Server(port);
            } catch (IllegalArgumentException i){
                System.err.println("Invalid port number");
            } catch (IOException e){
                System.err.println("Network error try again.");
            }


        }

        return server;

    }

}
