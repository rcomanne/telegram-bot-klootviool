package nl.rcomanne.telegrambotklootviool.handlers.command;

import nl.rcomanne.telegrambotklootviool.domain.InstaItem;
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

    long chatId;

    String query;

    final CommandType type;

    private final String botToken;

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
        if (isNull(image)) {
            return;
        }

        if (image.isAnimated()) {
            log.debug("sending animation '{}' from '{}' to chat '{}'", image.getId(), image.getSource(), this.chatId);
            if (sendAsAnimation(image)) {
                SendAnimation animation = new SendAnimation()
                    .setChatId(this.chatId)
                    .setCaption(image.getTitle())
                    .setAnimation(image.getImageLink());
                send(animation);
            } else {
                SendMessage message = new SendMessage()
                    .setChatId(this.chatId)
                    .setText(image.getTitle() + "\n" + image.getImageLink());
                send(message);
            }
        } else {
            log.debug("sending image '{}' from '{}' to chat '{}'", image.getId(), image.getSource(), this.chatId);
            SendPhoto photo = new SendPhoto().setChatId(this.chatId)
                .setPhoto(image.getImageLink())
                .setCaption(image.getTitle());
            send(photo);
        }
    }

    void sendItem(String message, SubredditImage image) {
        if (isNull(image)) {
            return;
        }

        if (image.isAnimated()) {
            log.debug("sending animation '{}' from '{}' to chat '{}'", image.getId(), image.getSource(), this.chatId);
            SendMessage sendMessage = new SendMessage().setChatId(this.chatId)
                .setText(message + "\n" + image.getTitle() + "\n" + image.getImageLink());
            send(sendMessage);
        } else {
            log.debug("sending image '{}' from '{}' to chat '{}'", image.getId(), image.getSource(), this.chatId);
            SendPhoto photo = new SendPhoto().setChatId(this.chatId)
                .setPhoto(image.getImageLink())
                .setCaption(message);
            send(photo);
        }
    }

    void sendItem(InstaItem item) {
        log.debug("sending item '{}' to chat '{}'", item.getId(), this.chatId);
        if (item.isVideo()) {
            SendAnimation animation = new SendAnimation()
                .setChatId(this.chatId)
                .setAnimation(item.getLink())
                .setCaption(item.getCaption());

            send(animation);
        } else {
            SendPhoto photo = new SendPhoto()
                .setChatId(this.chatId)
                .setPhoto(item.getLink())
                .setCaption(item.getCaption());

            send(photo);
        }
    }

    private void handleError(Exception exception) {
        log.warn("handling error with item... {}", exception.getMessage(), exception);
        try {
            SendMessage sendMessage = new SendMessage().setChatId(this.chatId)
                .setText("An exception occured: " + exception.getMessage());
            execute(sendMessage);
        } catch (Exception e) {
            log.error("exception while handling error: {}", e.getMessage());
        }
    }

    private boolean sendAsAnimation(SubredditImage image) {
        return false;
    }

    private boolean isNull(SubredditImage image) {
        if (image != null) {
            return false;
        } else {
            log.debug("retrieved a null reference, sending message.");
            SendMessage message = new SendMessage().setChatId(this.chatId)
                .setText("No images found... received NULL from findImage");
            send(message);
            return true;
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
}
