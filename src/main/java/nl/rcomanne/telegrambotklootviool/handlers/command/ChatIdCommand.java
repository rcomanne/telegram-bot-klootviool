package nl.rcomanne.telegrambotklootviool.handlers.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatIdCommand extends Command {

    private ChatIdCommand(final String token) {
        super(CommandType.UPDATE, token);
    }

    ChatIdCommand(final CommandParameters parameters, final String token) {
        this(token);
        this.chatId = parameters.getChatId();
        this.query = parameters.getQuery().orElse(null);
    }

    @Override
    void handle() {
        SendMessage message = new SendMessage()
            .setChatId(this.chatId)
            .setText("Hello! This is chat: " + this.chatId);

        send(message);
    }

    @Override
    void handleWithQuery() {
        handle();
    }
}
