package rudMad.Server;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

public class VoiceRelay implements AutoCloseable {

    private final DatagramSocket socket;
    private final ConcurrentHashMap<String, InetSocketAddress> participants = new ConcurrentHashMap<>();

    public VoiceRelay() throws SocketException {
        // Port 0 asks the operating system for an available local UDP port.
        socket = new DatagramSocket(0);
    }

    public int getPort() {
        return socket.getLocalPort();
    }

    /** Adds a participant or updates their UDP endpoint. */
    public void addParticipant(String username, InetSocketAddress udpAddress) {
        participants.put(username, udpAddress);
    }

    public void removeParticipant(String username) {
        participants.remove(username);
    }

    @Override
    public void close() {
        socket.close();
        participants.clear();
    }
}
