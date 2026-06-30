package rudMad.Protocol;

import java.io.IOException;

public interface HandShakeProtocol {

    String ACCEPTED = "ACCEPTED";
    String REJECTED = "REJECTED";

    boolean handshake() throws IOException;

    

}
