package nl.rcomanne.telegrambotklootviool.service;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService extends DefaultAbsSender {

    @Value("${bot.token}")
    private String token;

    private final SubredditImageService imageService;

    public MessageService(final SubredditImageService imageService) {
        super(ApiContext.getInstance(DefaultBotOptions.class));
        this.imageService = imageService;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public void sendMessage(String chatId, String message) {
        log.info("sending message to {}", chatId);
        SubredditImage image = imageService.findRandom();
        SendPhoto sendPhoto = new SendPhoto()
            .setPhoto(image.getImageLink())
            .setChatId(chatId)
            .setCaption(message);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException ex) {
            log.warn("unable to sendMessage {}", ex.getMessage(), ex);
        }
    }
}