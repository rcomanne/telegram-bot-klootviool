package nl.rcomanne.telegrambotklootviool.utility;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import static nl.rcomanne.telegrambotklootviool.utility.CommandUtility.getCleanCommandName;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandUtilityTest {
    private static final String BOT_NAME = "tester_bot";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void getCommand() {
        MessageEntity entity = mock(MessageEntity.class);
        final String testCommand = "testing";
        when(entity.getText()).thenReturn("/" + testCommand);

        String command = getCleanCommandName(entity, BOT_NAME);
        assertEquals(testCommand, command);
    }

    @Test
    public void getCommandWithBotName() {
        MessageEntity entity = mock(MessageEntity.class);
        final String testCommand = "testing";
        when(entity.getText()).thenReturn("/" + testCommand + "@" + BOT_NAME);

        String command = getCleanCommandName(entity, BOT_NAME);
        assertEquals(testCommand, command);
    }

    @Test
    public void getCommandDifferentBotName() {
        MessageEntity entity = mock(MessageEntity.class);
        final String testCommand = "testing";
        when(entity.getText()).thenReturn("/" + testCommand + "@" + "something");

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("command is not for us, does not mention us");

        getCleanCommandName(entity, BOT_NAME);
    }
}