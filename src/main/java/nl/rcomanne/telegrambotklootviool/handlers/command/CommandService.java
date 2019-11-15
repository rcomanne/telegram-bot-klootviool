package nl.rcomanne.telegrambotklootviool.handlers.command;

import static nl.rcomanne.telegrambotklootviool.utility.CommandUtility.getCommandType;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommandService {
    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.name}")
    private String botName;

    private final CommandFactory commandFactory;

    @Autowired
    public CommandService(final CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public void handle(MessageEntity entity, long chatId) {
        log.info("handling command: '{}'", entity.getText());

        final CommandParameters parameters = new CommandParameters(
            getCommandType(entity, botName),
            chatId,
            Optional.empty());

        doHandle(parameters);
    }

    public void handleWithQuery(MessageEntity entity, long chatId, String query) {
        log.info("handling command '{}' with query '{}'",entity.getText(), query);

        final CommandParameters parameters = new CommandParameters(
            getCommandType(entity, botName),
            chatId,
            Optional.of(query));

        doHandle(parameters);
    }

    private void doHandle(CommandParameters parameters) {
        try {
            Command command = commandFactory.create(parameters);
            new Thread(command).start();
        } catch (Exception e) {
            log.warn("command {} failed", parameters.getType(), e);
        }
    }
}
