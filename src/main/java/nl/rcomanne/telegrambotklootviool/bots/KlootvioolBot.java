package nl.rcomanne.telegrambotklootviool.bots;

import java.util.List;

import nl.rcomanne.telegrambotklootviool.handlers.command.CommandService;
import nl.rcomanne.telegrambotklootviool.handlers.message.MessageHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.BotOptions;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KlootvioolBot implements LongPollingBot {
    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    private final BotOptions options;
    private final CommandService commandService;
    private final MessageHandler messageHandler;

    public KlootvioolBot(final CommandService commandService, final MessageHandler messageHandler) {
        this.commandService = commandService;
        this.messageHandler = messageHandler;
        this.options = new DefaultBotOptions();
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("processing update: {}:{}", update.getUpdateId(), update.getMessage());
        if (update.hasInlineQuery()) {
            // we do not handle inline queries now
            return;
        }

        final long chatId = update.getMessage().getChatId();
        if (update.getMessage().isCommand()) {
            String message = update.getMessage().getText();
            log.info("received command {}", message);
            List<MessageEntity> entities = update.getMessage().getEntities();
            if (message.contains(" ")) {
                for (MessageEntity entity : entities) {
                    commandService.handleWithQuery(entity, chatId, message.substring(message.indexOf(' ') + 1));
                }
            } else {
                for (MessageEntity entity : entities) {
                    commandService.handle(entity, chatId);
                }
            }

        } else if (update.getMessage().hasText()) {
            messageHandler.handle(update);
        }
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public String getBotUsername() {
        return this.username;
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        log.debug("received {} updates", updates.size());
        updates.parallelStream().forEach(this::onUpdateReceived);
    }

    @Override
    public BotOptions getOptions() {
        return this.options;
    }

    @Override
    public void clearWebhook() {
        // do nothing -- no webhook
    }
}
