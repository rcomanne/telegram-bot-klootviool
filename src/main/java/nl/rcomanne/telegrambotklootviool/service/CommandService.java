package nl.rcomanne.telegrambotklootviool.service;

import static nl.rcomanne.telegrambotklootviool.utility.CommandUtility.getCleanCommandName;

import nl.rcomanne.telegrambotklootviool.command.MemeCommandService;
import nl.rcomanne.telegrambotklootviool.command.PicCommandService;
import nl.rcomanne.telegrambotklootviool.command.ProgrammerHumorCommandService;
import nl.rcomanne.telegrambotklootviool.command.QuoteCommandService;
import nl.rcomanne.telegrambotklootviool.command.SubredditCommandService;
import nl.rcomanne.telegrambotklootviool.command.UpdateCommandService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandService {

    private final MemeCommandService memeCommandService;

    private final PicCommandService picCommandService;

    private final ProgrammerHumorCommandService programmerHumorCommandService;

    private final QuoteCommandService quoteCommandService;

    private final SubredditCommandService subredditCommandService;

    private final UpdateCommandService updateCommandService;

    @Value("${bot.username}")
    private String username;

    public void process(final long chatId, final MessageEntity entity) {
        String command = getCleanCommandName(entity, username);
        log.debug("processing command {}", command);
        switch (command) {
            case "quote":
                quoteCommandService.handle(chatId);
                break;
            case "ph":
                programmerHumorCommandService.handle(chatId);
                break;
            case "memes":
                memeCommandService.handle(chatId);
                break;
            case "pic":
                picCommandService.handle(chatId);
                break;
            case "subreddit":
            case "r":
                subredditCommandService.handle(chatId);
                break;
            case "update":
                updateCommandService.handle(chatId);
                break;
            default:
                throw new IllegalArgumentException("unknown command: " + command);
        }
    }

    public void processWithQuery(final long chatId, final MessageEntity entity, final String query) {
        String command = getCleanCommandName(entity, username);
        log.debug("processing command {} with query {}", command, query);
        switch (command) {
            case "quote":
                quoteCommandService.handle(chatId, query);
                break;
            case "ph":
                programmerHumorCommandService.handle(chatId, query);
                break;
            case "memes":
                memeCommandService.handle(chatId, query);
                break;
            case "pic":
                picCommandService.handle(chatId, query);
                break;
            case "subreddit":
            case "r":
                subredditCommandService.handle(chatId, query);
                break;
            case "update":
                updateCommandService.handle(chatId, query);
                break;
            default:
                throw new IllegalArgumentException("unknown command: " + command);
        }
    }
}
