package rudMad.IntegrationTest;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rudMad.Client.Client;
import rudMad.Client.Commands.CommandHandler;

public class JoinVoiceCommandIntegrationTest {


    private Client client;
    CommandHandler handler;

    final String command = "/joinvoice";

    @BeforeEach
    void setup(){
        
        client = mock(Client.class);
        handler = new CommandHandler(client);
    }



    @Test
    void InvalidPortabc() throws IOException{

        String args = "/joinvoice abc";

        handler.handle(args);

        verify(client, never()).openUDPPort(anyInt());
        verify(client, never()).sendMessage(anyString());
        
    }

    @Test
    void InvalidPortGreaterThan65535() throws IOException{
        
        String args  = command + " 65538";

        handler.handle(args);

        verify(client, never()).openUDPPort(anyInt());
        verify(client, never()).sendMessage(anyString());

    }

    @Test 
    void InvalidPortLessThan1() throws IOException{

        String args = command + " -2";

        handler.handle(args); 

        verify(client, never()).openUDPPort(anyInt());
        verify(client, never()).sendMessage(anyString());

    }

    @Test
    void CorrectInput() throws IOException {

        String args = command + " 9001";

        when(client.openUDPPort(9001)).thenReturn(true);


        handler.handle(args);

        verify(client).openUDPPort(9001);

        verify(client).sendMessage(command + " 9001");
    
    }

    @Test
    void InvalidCommandSyntax() throws IOException{

        String args = command;

        handler.handle(args);

        verify(client, never()).openUDPPort(anyInt());
        verify(client, never()).sendMessage(anyString());

    }



}
