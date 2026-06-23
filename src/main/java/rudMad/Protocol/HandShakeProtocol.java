package rudMad.Protocol;

public interface HandShakeProtocol {

    String ACCEPTED = "ACCEPTED";
    String REJECTED = "REJECTED";

    boolean handshake();

    

}
