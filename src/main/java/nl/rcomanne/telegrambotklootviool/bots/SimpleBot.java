package nl.rcomanne.telegrambotklootviool.bots;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.service.CommandService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
public class SimpleBot extends TelegramLongPollingBot {
    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    private final CommandService commandService;

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("processing update: {}", update);
        if (update.hasInlineQuery()) {
            // we do not handle inline queries now
            return;
        }

        if (update.hasMessage()) {
            final long chatId = update.getMessage().getChatId();
            if (update.getMessage().isCommand()) {
                // update is a command, pass it on to the command service
                String message = update.getMessage().getText();
                log.debug("received command {}", message);
                List<MessageEntity> entities = update.getMessage().getEntities();
                if (message.contains(" ")) {
                    for (MessageEntity entity : entities) {
                        commandService.processWithQuery(chatId, entity, message.substring(message.indexOf(' ') + 1));
                    }
                } else {
                    for (MessageEntity entity : entities) {
                        commandService.process(chatId, entity);
                    }
                }
            }
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
        for (Update update : updates) {
            onUpdateReceived(update);
        }
    }
}
