package rudMad.Client.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UdpConnection implements AutoCloseable{

    private DatagramSocket socket;

    /** Opens a UDP socket bound to the local port. */
    public UdpConnection(int localPort) throws IOException {
        socket = new DatagramSocket(localPort);
    }

    /** Sets the remote destination for this socket. */
    public void connect(String host, int remotePort) throws IOException {
        socket.connect(new InetSocketAddress(host, remotePort));
    }

    @Override
    public void close() {
        socket.close();
    }


    public void send(byte[] data) throws IOException {

        DatagramPacket packet = new DatagramPacket(data, data.length);

        socket.send(packet);
    }
}
