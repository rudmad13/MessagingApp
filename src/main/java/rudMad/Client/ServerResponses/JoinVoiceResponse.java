package rudMad.Client.ServerResponses;

import rudMad.Client.Client;

public class JoinVoiceResponse implements ServerResponse {

    @Override
    public void onSuccess(Client client, String[] args) {
        if (client.getUdpConnection() == null) {
            System.err.println("No local UDP port is open for this voice response.");
            return;
        }

        int remotePort = Integer.parseInt(args[2]);

        if (!client.ConnectToServer(remotePort)) {
            client.closeUdpConnection();
            return;
        }

        System.out.println("Voice UDP destination configured on server port " + remotePort + ".");
    }

    @Override
    public void OnFailure(Client client, String[] args) {
        client.closeUdpConnection();
        System.out.println("Voice join was unsuccessful.");
    }
}
