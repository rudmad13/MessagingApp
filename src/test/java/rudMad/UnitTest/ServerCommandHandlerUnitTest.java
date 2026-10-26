package rudMad.UnitTest;

import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rudMad.Server.ClientHandler;
import rudMad.Server.Commands.Command;
import rudMad.Server.Commands.CommandHandler;

public class ServerCommandHandlerUnitTest {

    private ClientHandler client;
    private Command command;
    private CommandHandler handler;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() throws ReflectiveOperationException {
        client = mock(ClientHandler.class);
        command = mock(Command.class);
        handler = new CommandHandler(client);
        // Insert a test command without adding a registration API to production code.
        Field commandsField = CommandHandler.class.getDeclaredField("commands");
        commandsField.setAccessible(true);
        Map<String, Command> commands = (Map<String, Command>) commandsField.get(handler);
        commands.put("joinvoice", command);
    }

    @Test
    void delegatesArgumentsToRegisteredCommand() throws IOException {
        handler.handle("/joinvoice   9001\t extra  ");

        verify(command).execute(same(client), aryEq(new String[] {"9001", "extra"}));
    }

    @Test
    void delegatesMissingArgumentsForCommandToValidate() throws IOException {
        handler.handle("/joinvoice");

        verify(command).execute(same(client), aryEq(new String[0]));
    }

    @Test
    void unknownEmptyAndNonCommandInputsDoNotExecuteCommands() throws IOException {
        handler.handle("/unknown 9001");
        handler.handle("/");
        handler.handle("/   ");
        handler.handle("hello");
        handler.handle(null);

        verifyNoInteractions(command);
    }
}
