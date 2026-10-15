package rudMad.Client.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UdpConnection implements AutoCloseable{

    private DatagramSocket socket;

    public UdpConnection(String host, int port) throws IOException{

        socket = new DatagramSocket(port); //open local port for communication

        InetSocketAddress address = new InetSocketAddress(host, port);

        socket.connect(address);
        
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
