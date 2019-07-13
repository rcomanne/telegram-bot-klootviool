package nl.rcomanne.telegrambotklootviool.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import static nl.rcomanne.telegrambotklootviool.utility.CommandUtility.getCleanCommandName;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandService {
    @Value("${bot.username}")
    private String username;

    private final QuoteCommandService quoteCommandService;
    private final ProgrammerHumorCommandService programmerHumorCommandService;

    public void process(final long chatId, final MessageEntity entity) {
        log.debug("processing command");
        String command = getCleanCommandName(entity, username);
        switch (command) {
            case "quote":
                quoteCommandService.handle(chatId);
                break;
            case "ph":
                programmerHumorCommandService.handle(chatId);
                break;
            default:
                throw new IllegalArgumentException("unknown command: " + command);
        }
    }

    public void processWithQuery(final long chatId, final MessageEntity entity, final String query) {
        log.debug("processing command with query");
        String command = getCleanCommandName(entity, username);
        switch (command) {
            case "quote":
                quoteCommandService.handleWithQuery(chatId, query);
                break;
            case "ph":
                programmerHumorCommandService.handleWithQuery(chatId, query);
                break;
            default:
                throw new IllegalArgumentException("unknown command: " + command);
        }
    }
}
