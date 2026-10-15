package rudMad.Client.Commands;

import rudMad.Client.Client;

public interface Command {

    public void execute(Client client, String[] args);

}
