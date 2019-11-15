package nl.rcomanne.telegrambotklootviool.utility;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import static nl.rcomanne.telegrambotklootviool.utility.CommandUtility.getCleanCommandName;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.rcomanne.telegrambotklootviool.handlers.command.CommandType;

public class CommandUtilityTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private static final String BOT_NAME = "test-bot";
    private static final String BOT_NAME_CAPS = "TEST-BOT";

    private MessageEntity mockEntity;

    @Before
    public void setUp() {
        mockEntity = mock(MessageEntity.class);
    }

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

    @Test
    public void testGetInvalidCommand() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("unknown type testy");

        when(mockEntity.getText()).thenReturn("/testy");
        CommandType type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
    }

    @Test
    public void testGetCommand() {
        // lowercase
        when(mockEntity.getText()).thenReturn("/update");
        CommandType type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.UPDATE, type);

        // all caps
        when(mockEntity.getText()).thenReturn("/UPDATE");
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.UPDATE, type);

        // lower and upper case
        when(mockEntity.getText()).thenReturn("/UpDaTE");
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.UPDATE, type);
    }

    @Test
    public void testGetCommandWithBotName() {
        // lowercase
        when(mockEntity.getText()).thenReturn("/update@" + BOT_NAME);
        CommandType type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.UPDATE, type);

        // command all caps
        when(mockEntity.getText()).thenReturn("/UPDATE@" + BOT_NAME);
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.UPDATE, type);

        // command lower and upper case
        when(mockEntity.getText()).thenReturn("/uPDaTe@" + BOT_NAME);
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.UPDATE, type);

        // lowercase
        when(mockEntity.getText()).thenReturn("/update@" + BOT_NAME_CAPS);
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.UPDATE, type);

        // command all caps
        when(mockEntity.getText()).thenReturn("/UPDATE@" + BOT_NAME_CAPS);
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.UPDATE, type);

        // command lower and upper case
        when(mockEntity.getText()).thenReturn("/uPDaTe@" + BOT_NAME_CAPS);
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.UPDATE, type);
    }

    @Test
    public void testAllCommands() {
        // test pic command
        when(mockEntity.getText()).thenReturn("/pic");
        CommandType type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.PIC, type);

        // test subreddit command
        when(mockEntity.getText()).thenReturn("/subreddit");
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.SUBREDDIT, type);

        when(mockEntity.getText()).thenReturn("/r");
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.SUBREDDIT, type);

        // test update command
        when(mockEntity.getText()).thenReturn("/update");
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.UPDATE, type);

        // test chatId command
        when(mockEntity.getText()).thenReturn("/chatId");
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.CHAT_ID, type);
    }

    @Test
    public void testAllCommandsWithBotName() {
        // test update command
        when(mockEntity.getText()).thenReturn("/update@" + BOT_NAME);
        CommandType type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.UPDATE, type);

        // test pic command
        when(mockEntity.getText()).thenReturn("/pic@" + BOT_NAME);
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.PIC, type);

        // test subreddit command
        when(mockEntity.getText()).thenReturn("/subreddit@" + BOT_NAME);
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.SUBREDDIT, type);

        when(mockEntity.getText()).thenReturn("/r@" + BOT_NAME);
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.SUBREDDIT, type);

        // test update command
        when(mockEntity.getText()).thenReturn("/update@" + BOT_NAME);
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.UPDATE, type);

        // test chatId command
        when(mockEntity.getText()).thenReturn("/chatId@" + BOT_NAME);
        type = CommandUtility.getCommandType(mockEntity, BOT_NAME);
        assertEquals(CommandType.CHAT_ID, type);
    }
}
