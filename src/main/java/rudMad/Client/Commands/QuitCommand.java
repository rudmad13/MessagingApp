package rudMad.Client.Commands;

import rudMad.Client.Client;

public class QuitCommand implements Command{



    @Override
    public void execute(Client client, String[] args) {

        client.closeTlsConnection();
        client.closeUdpConnection();
    }

}
