package rudMad.Protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

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

    @Override
    public boolean handshake() throws IOException {
        out.write(username);
        out.newLine();
        out.flush();
        String response = in.readLine();
        if (ACCEPTED.equals(response)) {
            return true;
        }
        if (REJECTED.equals(response)) {
            return false;
        }
        throw new IOException(response == null
                ? "Server closed the connection during username handshake."
                : "Unexpected server response during username handshake.");
    }
}
