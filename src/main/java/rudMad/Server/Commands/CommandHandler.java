package rudMad.Server.Commands;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

import rudMad.Server.ClientHandler;
import rudMad.Server.Server;

public class CommandHandler {

    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final HashMap<String, Command> commands = new HashMap<>();
    private final ClientHandler client;

    public CommandHandler(ClientHandler client) {
        this.client = client;
        commands.put("joinvoice", new JoinVoiceCommand());
    }

    public void handle(String input) throws IOException {
        if (input == null || !input.startsWith("/")) {
            return;
        }

        String commandText = input.substring(1).trim();
        if (commandText.isEmpty()) {
            return;
        }

        String[] parts = commandText.split("\\s+");
        Command command = commands.get(parts[0]);
        if (command == null) {
            logger.warning(() -> "Unknown command from " + client.getUsername() + ": " + parts[0]);
            return;
        }

        command.execute(client, Arrays.copyOfRange(parts, 1, parts.length));
    }
}
