package nl.rcomanne.telegrambotklootviool.handlers.command;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;

import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class Command extends DefaultAbsSender implements Runnable {
    private final String botToken;

    final CommandType type;

    long chatId;

    String query;

    public Command(final CommandType type, final String botToken) {
        super(ApiContext.getInstance(DefaultBotOptions.class));
        this.botToken = botToken;
        this.type = type;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    @Override
    public void run() {
        log.trace("started new thread for command");
        try {
            if (this.query != null) {
                handleWithQuery();
            } else {
                handle();
            }
        } catch (IllegalArgumentException ex) {
            log.error("failed to handle command: {}", ex.getMessage(), ex);
            handleError(ex);
        }
    }

    /**
     * Handle the command without query parameters
     */
    abstract void handle();

    /**
     * Handle the command with query parameters
     */
    abstract void handleWithQuery();

    private void handleError(Exception exception) {
        log.debug("handling error with photo... {}", exception.getMessage());
        try {
            SendMessage sendMessage = new SendMessage()
                .setChatId(this.chatId)
                .setText("An exception occured: " + exception.getMessage());

            execute(sendMessage);
        } catch (Exception e) {
            log.error("exception while handling error: {}", e.getMessage());
        }
    }

    void send(SendPhoto sendPhoto) {
        log.debug("sending photo to chat: {}", sendPhoto.getChatId());
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    void send(SendMessage sendMessage) {
        log.debug("sending message to chat: {}", sendMessage.getChatId());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    void send(SendAnimation sendAnimation) {
        log.debug("sending animation to chat: {}", sendAnimation.getChatId());
        try {
            execute(sendAnimation);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    void sendItem(SubredditImage image) {
        if (isNull(image)) return;

        log.debug("sending image '{}' to chat '{}'", image.getId(), this.chatId);
        if (false || image.isAnimated()) {
            SendAnimation animation = new SendAnimation()
                .setChatId(this.chatId)
                .setAnimation(image.getImageLink())
                .setCaption(image.getTitle());

            send(animation);
        } else {
            SendPhoto photo = new SendPhoto()
                .setChatId(this.chatId)
                .setPhoto(image.getImageLink())
                .setCaption(image.getTitle());

            send(photo);
        }
    }

    void sendItem(String message, SubredditImage image) {
        if (isNull(image)) return;

        if (false || image.isAnimated()) {
            SendAnimation animation = new SendAnimation()
                .setChatId(this.chatId)
                .setAnimation(image.getImageLink())
                .setCaption(message);
            send(animation);
        } else {
            SendPhoto photo = new SendPhoto()
                .setChatId(this.chatId)
                .setPhoto(image.getImageLink())
                .setCaption(message);
            send(photo);
        }
    }

    private boolean isNull(SubredditImage image) {
        if (image != null) {
            return false;
        } else {
            log.debug("retrieved a null reference, sending message.");
            SendMessage message = new SendMessage()
                .setChatId(this.chatId)
                .setText("No images found... received NULL from findImage");
            send(message);
            return true;
        }
    }
}
