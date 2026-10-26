package rudMad.Server.Commands;

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import rudMad.Server.ClientHandler;
import rudMad.Server.Server;

public class JoinVoiceCommand implements Command {

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    @Override
    public void execute(ClientHandler client, String[] args) throws IOException {
        if (args.length != 1) {
            client.sendMessage("/failure joinvoice");
            return;
        }

        int clientPort;
        try {
            clientPort = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            client.sendMessage("/failure joinvoice");
            return;
        }

        if (clientPort < 1 || clientPort > 65535) {
            client.sendMessage("/failure joinvoice");
            return;
        }

        int serverPort;
        try {
            serverPort = client.joinVoice(clientPort);
        } catch (SocketException e) {
            logger.log(Level.WARNING, "Unable to open the voice relay", e);
            client.sendMessage("/failure joinvoice");
            return;
        }

        // A failed reply propagates to receiveMessage(), which removes the client.
        client.sendMessage("/success joinvoice " + serverPort);
    }
}
