package rudMad.UnitTest;

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
import rudMad.Client.ServerResponses.JoinVoiceResponse;
import rudMad.Client.UDP.UdpConnection;

public class JoinVoiceResponseUnitTest {

    private Client client;
    private JoinVoiceResponse response;

    @BeforeEach
    void setup() {
        client = mock(Client.class);
        response = new JoinVoiceResponse();
    }

    @Test
    void successConnectsToRemotePort() {
        // The handler removes the slash and splits the server message into tokens.
        String[] args = {"success", "joinvoice", "9001"};
        when(client.getUdpConnection()).thenReturn(mock(UdpConnection.class));
        when(client.ConnectToServer(9001)).thenReturn(true);

        assertTrue(response.onSuccess(client, args));

        verify(client).ConnectToServer(9001);
        verify(client, never()).closeUdpConnection();
    }

    @Test
    void successWithoutLocalUdpConnectionReturnsFalse() {
        String[] args = {"success", "joinvoice", "9001"};
        when(client.getUdpConnection()).thenReturn(null);

        assertFalse(response.onSuccess(client, args));

        verify(client, never()).ConnectToServer(anyInt());
        verify(client, never()).closeUdpConnection();
    }

    @Test
    void failedConnectionClosesUdpConnection() {
        String[] args = {"success", "joinvoice", "9001"};
        when(client.getUdpConnection()).thenReturn(mock(UdpConnection.class));
        when(client.ConnectToServer(9001)).thenReturn(false);

        assertFalse(response.onSuccess(client, args));

        verify(client).ConnectToServer(9001);
        verify(client).closeUdpConnection();
    }

    @Test
    void failureClosesUdpConnectionWithoutRemotePort() {
        String[] args = {"failure", "joinvoice"};

        assertFalse(response.OnFailure(client, args));

        verify(client).closeUdpConnection();
        verify(client, never()).ConnectToServer(anyInt());
    }
}
