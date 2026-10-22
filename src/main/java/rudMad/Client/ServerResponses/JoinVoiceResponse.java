package rudMad.Client.ServerResponses;

import rudMad.Client.Client;

public class JoinVoiceResponse implements ServerResponse{

    @Override
    public void onSuccess(Client client, String[] args) {
        //Open port and "connect" to the server port given to us
        
    }

    @Override
    public void OnFailure(Client client, String[] args) {

        //Print to the console the command was unsuccesful

    }


}
