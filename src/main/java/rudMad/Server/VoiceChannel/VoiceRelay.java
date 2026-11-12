package rudMad.Server.VoiceChannel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

public class VoiceRelay implements AutoCloseable, Runnable{

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

    public boolean checkSender(InetSocketAddress address){
        return participants.contains(address);
    }

    public void forwardPacket(DatagramPacket packet){
        InetSocketAddress sender = (InetSocketAddress) packet.getSocketAddress();
        for(InetSocketAddress address : participants.values()){
            if(address.equals(sender)){
                continue;
            }

            //Set new destination ip and forward packet
            packet.setSocketAddress(address);
            try{
            socket.send(packet);
            } catch (IOException e){
                //Come back and maybe and logging for error in packet forwarding
                System.err.println("Unable to forward packet error occured");

            }
        }
    }

    @Override
    public void close() {
        socket.close();
        participants.clear();
    }

    /**
     * This thread will hear for incoming packets on the socket.
     * Checks if the sender is in the voice session, if the address is valid then it will forward the packets to others on the session
     * 
     */
    @Override
    public void run() {

        byte[] buffer = new byte[640];//With current audio format this should equates to about 20 milliseconds of audio
        DatagramPacket payload = new DatagramPacket(buffer,buffer.length);
        try {
            while (!Thread.currentThread().isInterrupted()) {
            payload.setLength(buffer.length);
            socket.receive(payload);

            if (checkSender((InetSocketAddress) payload.getSocketAddress())) {
                forwardPacket(payload);
            }
            }
        } catch (IOException e) {
            if (!socket.isClosed()) {
            System.err.println("Error receiving packets: " + e.getMessage());
            }
        } finally {
            close();
        }
    }
}


