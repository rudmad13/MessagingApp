package rudMad.Client.ServerResponses;

import rudMad.Client.Client;

public interface ServerResponse {

    void onSuccess(Client client, String[] args);

    void OnFailure(Client client, String[] args);

}
