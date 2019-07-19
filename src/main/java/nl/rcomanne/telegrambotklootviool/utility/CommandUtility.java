package nl.rcomanne.telegrambotklootviool.utility;

import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandUtility {
    private CommandUtility() {}

    public static String getCleanCommandName(MessageEntity entity,  String botName) {
        // get full command excluding the preceding /
        String command = entity.getText().substring(1);
        log.debug("command: {}, botname: {}", command, botName);
        if (command.contains("@")) {
            if (command.contains(botName)) {
                // command contains a mention, and it is for us
                return command.substring(0, command.indexOf('@'));
            } else {
                // command has mention, but not for us
                throw new IllegalArgumentException("command is not for us, does not mention us");
            }
        }
        return command;
    }
}
