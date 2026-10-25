package rudMad.Client.ServerResponses;

import rudMad.Client.Client;

public class JoinVoiceResponse implements ServerResponse {

    @Override
    public boolean onSuccess(Client client, String[] args) {
        if (client.getUdpConnection() == null) {
            System.err.println("No local UDP port is open for this voice response.");
            return false;
        }

        int remotePort = Integer.parseInt(args[2]);

        if (!client.ConnectToServer(remotePort)) {
            client.closeUdpConnection();
            return false;
        }

        System.out.println("Voice UDP destination configured on server port " + remotePort + ".");
        return true;
    }

    @Override
    public boolean OnFailure(Client client, String[] args) {
        client.closeUdpConnection();
        System.out.println("Voice join was unsuccessful.");
        return false;
    }
}
