package nl.rcomanne.telegrambotklootviool.handlers.command;

import nl.rcomanne.telegrambotklootviool.service.GoogleSearchService;
import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommandFactory {
    private final GoogleSearchService searchService;
    private final SubredditService subredditService;

    @Value("${bot.token}")
    private String botToken;

    Command create(CommandParameters parameters) {
        switch (parameters.getType()) {
            case PIC:
                return new PicCommand(parameters, botToken, searchService);
            case SUBREDDIT:
                return new SubredditCommand(parameters, botToken, subredditService);
            case UPDATE:
                return new UpdateCommand(parameters, botToken, subredditService);
            case CHAT_ID:
                return new ChatIdCommand(parameters, botToken);
            default:
                throw new IllegalArgumentException("unknown command type!");
        }
    }
}
