package nl.rcomanne.telegrambotklootviool.service;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService extends DefaultAbsSender {
    private static final int MAX_RETRY_COUNT = 5;

    @Value("${bot.token}")
    private String token;

    private final SubredditService imageService;

    public MessageService(final SubredditService imageService) {
        super(ApiContext.getInstance(DefaultBotOptions.class));
        this.imageService = imageService;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public void sendRandomPhoto(String chatId) {
        log.info("sending random photo to chat {}", chatId);
        int retryCount = 0;
        boolean success = false;
        do {
            retryCount++;
            SubredditImage image = imageService.findRandom();
            SendPhoto sendPhoto = new SendPhoto()
                .setChatId(chatId)
                .setPhoto(image.getImageLink())
                .setCaption(image.getTitle());
            try {
                execute(sendPhoto);
                success = true;
            } catch (TelegramApiException ex) {
                log.warn("failed to send image {}.", image.getImageLink(), ex);
            }
        } while (!success && retryCount < MAX_RETRY_COUNT);
    }

    public void sendMessageRandomPhoto(String chatId, String message) {
        log.info("sending message to {}", chatId);
        SubredditImage image = imageService.findRandom();
        sendMessageWithPhoto(chatId, message, image);
    }

    public void sendMessageWithPhoto(String chatId, String message, SubredditImage image) {
        log.info("send photo '{}' with message '{}'", image.getImageLink(), message);
        SendPhoto sendPhoto = new SendPhoto()
            .setPhoto(image.getImageLink())
            .setChatId(chatId)
            .setCaption(message);
        doSendPhoto(sendPhoto);
    }

    private void doSendPhoto(SendPhoto sendPhoto) {
        int retryCount = 0;
        boolean success = false;
        do {
            try {
                retryCount++;
                execute(sendPhoto);
                success = true;
            } catch (TelegramApiException ex) {
                log.warn("failed to send photo with url {}", sendPhoto.getPhoto().getAttachName());
            }
        } while (!success && retryCount < MAX_RETRY_COUNT);
    }

    public void sendAnimation(SendAnimation animation) {
        int tryCount = 0;
        boolean success = false;
        do {
            try {
                tryCount++;
                execute(animation);
                success = true;
            } catch (TelegramApiRequestException ex) {
                log.warn("Reponse code: {}", ex.getErrorCode());
                log.warn("Response: {}", ex.getApiResponse());
                log.warn("failed to send animation.", ex);
            } catch (TelegramApiException e) {
                log.warn("failed to send animation.", e);
            }
        } while (!success && tryCount < 2);

    }
}
