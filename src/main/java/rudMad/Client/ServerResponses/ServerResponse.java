package rudMad.Client.ServerResponses;

import rudMad.Client.Client;

public interface ServerResponse {

    boolean onSuccess(Client client, String[] args);

    boolean OnFailure(Client client, String[] args);

}
