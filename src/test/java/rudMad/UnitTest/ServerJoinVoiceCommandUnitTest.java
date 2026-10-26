package rudMad.UnitTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.SocketException;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import rudMad.Server.ClientHandler;
import rudMad.Server.Commands.JoinVoiceCommand;

public class ServerJoinVoiceCommandUnitTest {

    private ClientHandler client;
    private JoinVoiceCommand command;

    @BeforeEach
    void setup() {
        client = mock(ClientHandler.class);
        command = new JoinVoiceCommand();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 9001, 65535})
    void validPortJoinsAndReturnsServerPort(int port) throws IOException {
        when(client.joinVoice(port)).thenReturn(54000);

        command.execute(client, new String[] {Integer.toString(port)});

        verify(client).joinVoice(port);
        verify(client).sendMessage("/success joinvoice 54000");
        verify(client, never()).sendMessage("/failure joinvoice");
    }

    static Stream<Arguments> invalidArguments() {
        return Stream.of(
                Arguments.of((Object) new String[0]),
                Arguments.of((Object) new String[] {"9001", "extra"}),
                Arguments.of((Object) new String[] {"abc"}),
                Arguments.of((Object) new String[] {"0"}),
                Arguments.of((Object) new String[] {"-1"}),
                Arguments.of((Object) new String[] {"65536"}),
                Arguments.of((Object) new String[] {"999999999999"}));
    }

    @ParameterizedTest
    @MethodSource("invalidArguments")
    void invalidInputReturnsFailureWithoutJoining(String[] args) throws IOException {
        command.execute(client, args);

        verify(client).sendMessage("/failure joinvoice");
        verify(client, never()).joinVoice(anyInt());
    }

    @Test
    void udpSetupFailureReturnsFailure() throws IOException {
        when(client.joinVoice(9001)).thenThrow(new SocketException("Cannot bind UDP socket"));

        command.execute(client, new String[] {"9001"});

        verify(client).sendMessage("/failure joinvoice");
        verify(client, never()).sendMessage("/success joinvoice 54000");
    }

    @Test
    void failedSuccessReplyPropagatesToConnectionCleanup() throws IOException {
        when(client.joinVoice(9001)).thenReturn(54000);
        doThrow(new IOException("Connection closed")).when(client).sendMessage("/success joinvoice 54000");

        assertThrows(IOException.class, () -> command.execute(client, new String[] {"9001"}));

        verify(client, never()).sendMessage("/failure joinvoice");
    }
}
