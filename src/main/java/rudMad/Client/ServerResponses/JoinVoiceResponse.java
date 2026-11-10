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

        //Link udp connection to host
        if (!client.ConnectToServer(remotePort)) {
            client.closeUdpConnection();
            return false;
        }

        //start voice session
        if(client.startVoiceSession()){

            System.out.println("Voice UDP destination configured on server port " + remotePort + ".");
            return true;
        }

        return false;
    }

    @Override
    public boolean OnFailure(Client client, String[] args) {
        client.closeUdpConnection();
        System.out.println("Voice join was unsuccessful. Closing local UDP port");
        return false;
    }
}
