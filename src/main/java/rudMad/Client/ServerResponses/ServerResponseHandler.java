package rudMad.Client.ServerResponses;

import rudMad.Client.Client;
import java.util.HashMap;


public class ServerResponseHandler {
    private final HashMap<String, ServerResponse> response = new HashMap<String, ServerResponse>();
    private Client client;

    public ServerResponseHandler(Client client){
        this.client = client;

        response.put("joinvoice", new JoinVoiceResponse());

    }


    public void handle(String message){

        String[] parts = message.substring(1).trim().split("\\s+");

        String status = parts[0];
        String commandName = parts[1];

        ServerResponse command = response.get(commandName);

        if(status.equals("successful")){
            command.onSuccess(client, parts);

        } else {
            command.OnFailure(client, parts);
        }


    }
    




}
