package rudMad.Protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ProtocolException;

/** Sends the username over the established TLS connection and awaits admission. */
public class ClientHandshake implements HandShakeProtocol {
    private final BufferedWriter out;
    private final BufferedReader in;
    private final String username;

    public ClientHandshake(BufferedWriter out, BufferedReader in, String username) {
        this.out = out;
        this.in = in;
        this.username = username;
    }

    /**
     * Recieves server response to validating username. 
     * @throws ProtocolExcpetion - If Username is already taken on the server
     * @throws IOException - Errors in the reading and writing of the streams
     */
    @Override
    public boolean handshake() throws ProtocolException, IOException {
        out.write(username);
        out.newLine();
        out.flush();
        String response = in.readLine();

        if (REJECTED.equals(response)) {
            throw new ProtocolException("User is taken!");
        }

        return true; 
    }
}
