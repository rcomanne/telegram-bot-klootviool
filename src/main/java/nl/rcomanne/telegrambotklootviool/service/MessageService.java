package nl.rcomanne.telegrambotklootviool.service;

import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.Subreddit;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

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

    public void sendMessageRandomPhoto(String chatId, String message) {
        log.info("sending message to {}", chatId);
        SubredditImage image = imageService.findRandom();
        sendMessageWithPhoto(chatId, message, image);
    }

    public void sendMessageRandomPhoto(String chatId, String message, Subreddit subreddit) {
        log.info("sending message to {}", chatId);
        SubredditImage image = imageService.findRandomBySubreddit(subreddit.getName(), chatId);
        assert image != null;
        sendMessageWithPhoto(chatId, message, image);
    }

    public void sendMessage(String chatId, String message) {
        log.info("sending message to {}", chatId);
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(message);
        doSendMessage(sendMessage);
    }

    public void sendMessageWithPhoto(String chatId, String message, SubredditImage image) {
        log.info("send photo '{}' with message '{}'", image.getImageLink(), message);
        SendMessage sendPhoto = new SendMessage()
            .setText(message + "\n" + image.getImageLink())
            .setChatId(chatId);
        doSendMessage(sendPhoto);
    }

    private void doSendMessage(SendMessage message) {
        int retryCount = 0;
        boolean success = false;
        do {
            try {
                retryCount++;
                execute(message);
                success = true;
            } catch (TelegramApiException ex) {
                log.warn("failed to send message {}", message);
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
