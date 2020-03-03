package nl.rcomanne.telegrambotklootviool.handlers.message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageHandler extends DefaultAbsSender {
    @Value("${bot.token}")
    private String botToken;

    public MessageHandler() {
        super(new DefaultBotOptions());
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void handle(Update update) {
        // simple help message

        final String name = update.getMessage().getFrom().getFirstName();
        final String response = String.format("STFU %s", name);

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
