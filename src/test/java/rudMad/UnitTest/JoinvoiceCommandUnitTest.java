package rudMad.UnitTest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rudMad.Client.Client;
import rudMad.Client.Commands.JoinVoiceCommand;

import static org.mockito.Mockito.*;

import java.io.IOException;

public class JoinvoiceCommandUnitTest {

    private Client client;
    private JoinVoiceCommand command;

    @BeforeEach
    void setup(){

        client = mock(Client.class);
        command = new JoinVoiceCommand();

    }



    @Test
    void invalidPortLetters() throws IOException{

        String[] args = {"abc"};

        command.execute(client, args);

        //verify that the port was never opened and sendMessage

        verify(client, never()).openUDPPort(anyInt());
        verify(client, never()).sendMessage(anyString());

    }

    @Test
    void invalidPortNumberGreaterThan65535() throws IOException {

        String[] args = {"65536"};

        command.execute(client, args);

        verify(client, never()).openUDPPort(anyInt());
        verify(client, never()).sendMessage(anyString());

    }

    @Test
    void invalidPortNumberLessThan1() throws IOException{

        String[] args = {"-2"};

        command.execute(client, args);

        verify(client, never()).openUDPPort(anyInt());
        verify(client, never()).sendMessage(anyString());

    }

    @Test
    void openUdpPortFailure() throws IOException {

        String[] args = {"9001"};
        
        when(client.openUDPPort(9001)).thenReturn(false);

        command.execute(client, args);

        verify(client).openUDPPort(9001);
        verify(client, never()).sendMessage(anyString());

    }

    @Test
    void correctInputSuccess() throws IOException{

        String[] args = {"9001"};

        when(client.openUDPPort(9001)).thenReturn(true);
        
        command.execute(client, args);

        verify(client).openUDPPort(9001);

        verify(client).sendMessage("/joinvoice 9001");

    }



}
