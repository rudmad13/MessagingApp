package rudMad.Client.Commands;

import java.io.IOException;

import rudMad.Client.Client;

public class JoinVoiceCommand implements Command{

    @Override
    public void execute(Client client, String[] args) {
        
        if (args.length != 2){
            System.out.println("Usage: /joinvoice <port>");
            return;
        }

        int port;

        try{
            port = Integer.parseInt(args[1]);

        } catch (NumberFormatException e){
            System.out.println("Port must be a number");
            return;
        }

        if (port < 1 || port > 65535){
            System.out.println("Port number must be between 1 and 65535");
            return;
        }

        if (!client.openUDPPort(port)) {
            return;
        }

        try{
            client.sendMessage("/joinvoice " + port);

        } catch (IOException e){
            client.closeUdpConnection();
            System.out.println("Failed to send voice request: " + e.getMessage());
        }
    }

    

}
