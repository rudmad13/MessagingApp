package rudMad.IntegrationTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rudMad.Client.Client;
import rudMad.Client.ServerResponses.ServerResponseHandler;
import rudMad.Client.UDP.UdpConnection;

// Exercises the real handler and response together, with no network connections.
public class JoinVoiceResponseIntegrationTest {

    private Client client;
    private ServerResponseHandler handler;

    @BeforeEach
    void setup() {
        client = mock(Client.class);
        handler = new ServerResponseHandler(client);
    }

    @Test
    void successMessageDelegatesWithRemotePort() {
        when(client.getUdpConnection()).thenReturn(mock(UdpConnection.class));
        when(client.ConnectToServer(9001)).thenReturn(true);

        assertTrue(handler.handle("/success joinvoice 9001"));

        verify(client).ConnectToServer(9001);
        verify(client, never()).closeUdpConnection();
    }

    @Test
    void failureMessageDelegatesWithoutRemotePort() {
        assertFalse(handler.handle("/failure joinvoice"));

        verify(client).closeUdpConnection();
        verify(client, never()).ConnectToServer(anyInt());
    }
}
