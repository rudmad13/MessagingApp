package rudMad.Server.Commands;

import java.io.IOException;

import rudMad.Server.ClientHandler;

public interface Command {

    void execute(ClientHandler client, String[] args) throws IOException;
}
