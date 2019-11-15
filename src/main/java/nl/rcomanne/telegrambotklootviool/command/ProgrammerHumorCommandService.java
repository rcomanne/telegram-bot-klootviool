package nl.rcomanne.telegrambotklootviool.command;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgrammerHumorCommandService extends AbstractCommandService {
    @Value("${bot.token}")
    private String token;

    private static final String SUBREDDIT = "programmerhumor";

    private final SubredditService service;

    public void handle(long chatId) {
        SubredditImage selected = service.findRandomBySubreddit(SUBREDDIT);
        sendSubredditImage(chatId, selected);
    }

    @Async
    public void handle(long chatId, String query) {
        SubredditImage selected = service.findBySubredditAndTitle(SUBREDDIT, query);
        sendSubredditImage(chatId, selected);
    }

    @Override
    public String getBotToken() {
        return this.token;
    }
}
