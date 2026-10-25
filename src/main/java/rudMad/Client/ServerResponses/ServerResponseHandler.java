package rudMad.Client.ServerResponses;

import java.util.HashMap;
import rudMad.Client.Client;

public class ServerResponseHandler {
    private final HashMap<String, ServerResponse> response = new HashMap<>();
    private final Client client;

    public ServerResponseHandler(Client client) {
        this.client = client;
        response.put("joinvoice", new JoinVoiceResponse());
    }

    public boolean handle(String message) {

        String[] parts = message.substring(1).trim().split("\\s+");

        String status = parts[0];
        

        ServerResponse command = response.get(parts[1]);
        if (command == null) {
            System.err.println("Unknown response command: " + parts[1]);
            return false;
        }

        if (status.equals("success")) {
            return command.onSuccess(client, parts);
        } else {
            return command.OnFailure(client, parts);
        }
    }
}
