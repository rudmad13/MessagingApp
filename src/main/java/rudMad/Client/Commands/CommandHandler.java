package rudMad.Client.Commands;

import java.util.HashMap;

import rudMad.Client.Client;

public class CommandHandler {

    private final HashMap<String , Command> commands = new HashMap<String, Command>();
    private Client client;


    public CommandHandler(Client client){
        this.client = client;

        commands.put("quit", new QuitCommand());
        commands.put("joinvoice", new JoinVoiceCommand());
    }


    public void handle(String input){

        String[] parts = input.substring(1).trim().split("\\s");

        Command command = commands.get(parts[0]);

        if(command == null){
            System.out.println("Unknown command " + parts[0]);
            return;
        }

        command.execute(client, parts);

    }

}
