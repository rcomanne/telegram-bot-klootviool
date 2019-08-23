package nl.rcomanne.telegrambotklootviool.command;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;

import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractCommandService extends DefaultAbsSender {
    public AbstractCommandService() {
        super(ApiContext.getInstance(DefaultBotOptions.class));
    }

    @Async
    public abstract void handle(final long chatId);

    @Async
    public abstract void handle(final long chatId, String query);

    void sendSubredditImage(final long chatId, final SubredditImage image) {
        if (image == null) {
            sendMessage(chatId, "no images found");
        } else {
            if (image.isAnimated()) {
                sendAnimation(chatId, image.getImageLink(), image.getTitle());
            } else {
                sendPhoto(chatId, image.getImageLink(), image.getTitle());
            }
        }
    }

    void sendMessage(final long chatId, final String message) {
        SendMessage sendMessage = new SendMessage()
            .setChatId(chatId)
            .setText(message);
        doSendMessage(sendMessage);
    }

    private void doSendMessage(final SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException ex) {
            log.warn("unable to sendMessage {}", ex.getMessage(), ex);
            sendError(sendMessage.getChatId(), ex);
        }
    }

    void sendAnimation(final long chatId, final String imageLink) {
        SendAnimation sendAnimation = new SendAnimation()
                .setChatId(chatId)
                .setAnimation(imageLink);
        doSendAnimation(sendAnimation);
    }

    void sendAnimation(final long chatId, final String imageLink, final String caption) {
        SendAnimation sendAnimation = new SendAnimation()
                .setChatId(chatId)
                .setCaption(caption)
                .setAnimation(imageLink);
        doSendAnimation(sendAnimation);
    }

    private void doSendAnimation(SendAnimation sendAnimation) {
        try {
            execute(sendAnimation);
        } catch (TelegramApiException ex) {
            log.warn("failed to sendAnimation {}", ex.getMessage(), ex);
            sendError(sendAnimation.getChatId(), ex);
        }
    }

    void sendPhoto(long chatId, final String imageLink) {
        SendPhoto sendPhoto = new SendPhoto()
                .setChatId(chatId)
                .setPhoto(imageLink);
        doSendPhoto(sendPhoto);
    }

    void sendPhoto(long chatId, final String imageLink, final String caption) {
        SendPhoto sendPhoto = new SendPhoto()
                .setChatId(chatId)
                .setCaption(caption)
                .setPhoto(imageLink);
        doSendPhoto(sendPhoto);
    }

    private void doSendPhoto(SendPhoto sendPhoto) {
        try {
            execute(sendPhoto);
        } catch (TelegramApiException ex) {
            log.warn("unable to sendPhoto {}", ex.getMessage(), ex);
            sendError(sendPhoto.getChatId(), ex);
        }
    }

    void sendError(final String chatId, final Exception exception) {
        SendMessage sendMessage = new SendMessage()
            .setChatId(chatId)
            .setText(exception.getMessage());

        doSendMessage(sendMessage);
    }

}
