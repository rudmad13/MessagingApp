package rudMad.IntegrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import rudMad.Server.ClientHandler;
import rudMad.Server.Server;

public class ServerCommandRoutingIntegrationTest {

    @Test
    void slashCommandsAreNotBroadcastAndChatContinuesUntilDisconnect() throws IOException {
        Socket socket = mock(Socket.class);
        Server server = mock(Server.class);
        String messages = "hello\n/joinvoice 9001\n/\n/unknown\nstill here\n";
        when(socket.getInputStream()).thenReturn(
                new ByteArrayInputStream(messages.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream replies = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(replies);
        InetAddress peerAddress = InetAddress.getByName("127.0.0.1");
        InetSocketAddress udpAddress = new InetSocketAddress(peerAddress, 9001);
        when(socket.getInetAddress()).thenReturn(peerAddress);
        when(server.joinVoice("test-user", udpAddress)).thenReturn(54000);
        ClientHandler client = new ClientHandler(socket, server);
        client.setUsername("test-user");

        client.receiveMessage();

        assertEquals("/success joinvoice 54000" + System.lineSeparator(), replies.toString(StandardCharsets.UTF_8));
        verify(server).joinVoice("test-user", udpAddress);
        verify(server).broadcast("hello", client);
        verify(server).broadcast("still here", client);
        verify(server).removeClient(client);
        verifyNoMoreInteractions(server);
    }
}
