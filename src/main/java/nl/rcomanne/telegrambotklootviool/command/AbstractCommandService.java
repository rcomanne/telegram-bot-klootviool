package nl.rcomanne.telegrambotklootviool.command;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public abstract class AbstractCommandService extends DefaultAbsSender {
    public AbstractCommandService() {
        super(ApiContext.getInstance(DefaultBotOptions.class));
    }

    abstract void handle(final long chatId);

    abstract void handle(final long chatId, String query);

    void sendAnimation(final long chatId, final String imageLink) {
        SendAnimation sendAnimation = new SendAnimation()
                .setChatId(chatId)
                .setAnimation(imageLink);
        doSendAnimation(sendAnimation);
    }

    void sendAnimationWithCaption(final long chatId, final String imageLink, final String caption) {
        SendAnimation sendAnimation = new SendAnimation()
                .setChatId(chatId)
                .setCaption(caption)
                .setAnimation(imageLink);
        doSendAnimation(sendAnimation);
    }

    private void doSendAnimation(SendAnimation sendAnimation) {
        try {
            execute(sendAnimation);
        } catch (TelegramApiException e) {
            log.debug("failed to send animation");
        }
    }

    void sendPhoto(long chatId, final String imageLink) {
        SendPhoto sendPhoto = new SendPhoto()
                .setChatId(chatId)
                .setPhoto(imageLink);
        doSendPhoto(sendPhoto);
    }

    void sendPhotoWithCaption(long chatId, final String imageLink, final String caption) {
        SendPhoto sendPhoto = new SendPhoto()
                .setChatId(chatId)
                .setCaption(caption)
                .setPhoto(imageLink);
        doSendPhoto(sendPhoto);
    }

    private void doSendPhoto(SendPhoto sendPhoto) {
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("failed to send image", e);
        }
    }
}
