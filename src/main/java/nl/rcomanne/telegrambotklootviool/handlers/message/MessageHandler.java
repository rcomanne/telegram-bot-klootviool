package nl.rcomanne.telegrambotklootviool.handlers.message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageHandler extends DefaultAbsSender {
    @Value("${bot.token}")
    private String botToken;

    @Value("${owner.id}")
    private int ownerId;

    public MessageHandler() {
        super(new DefaultBotOptions());
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void handle(Update update) {

        final User from = update.getMessage().getFrom();
        String response;

        if (from.getBot()) {
            // no inter bot chats
            return;
        }

        if (from.getId().equals(ownerId)) {
            // specials persons
            response = String.format("Hello %s, what a retards in this channel, amirite?", from.getFirstName());
        } else {
            response = String.format("STFU %s", from.getFirstName());
        }


        SendMessage message = new SendMessage()
            .setChatId(update.getMessage().getChatId())
            .setText(response);
        try {
            log.info("sending message");
            execute(message);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException: {}", e.getMessage(), e);
        }
    }
}
