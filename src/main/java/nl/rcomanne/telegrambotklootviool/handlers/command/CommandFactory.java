package nl.rcomanne.telegrambotklootviool.handlers.command;

import java.util.List;

import nl.rcomanne.telegrambotklootviool.service.GoogleSearchService;
import nl.rcomanne.telegrambotklootviool.service.instagram.InstagramService;
import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommandFactory {
    private final GoogleSearchService searchService;
    private final SubredditService subredditService;
    private final InstagramService instagramService;

    @Value("${bot.token}")
    private String botToken;

    @Value("#{'${subreddit.banned}'.split(',')}")
    private List<String> bannedSubs;

    Command create(CommandParameters parameters) {
        switch (parameters.getType()) {
            case PIC:
                return new PicCommand(parameters, botToken, searchService);
            case SUBREDDIT:
                return new SubredditCommand(parameters, botToken, subredditService, bannedSubs);
            case INSTA:
                return new InstagramCommand(parameters, botToken, instagramService);
            case UPDATE:
                return new UpdateCommand(parameters, botToken, subredditService);
            case CHAT_ID:
                return new ChatIdCommand(parameters, botToken);
            default:
                throw new IllegalArgumentException("unknown command type!");
        }
    }
}
