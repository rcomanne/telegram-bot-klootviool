package nl.rcomanne.telegrambotklootviool.command;

import nl.rcomanne.telegrambotklootviool.service.GoogleSearchService;
import nl.rcomanne.telegrambotklootviool.service.SubredditImageService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PicCommandService extends AbstractCommandService {
    @Value("${bot.token}")
    private String token;

    private static final String DEF_SUBREDDIT = "memes";

    private final GoogleSearchService googleSearchService;
    private final SubredditImageService service;

    @Override
    public void handle(long chatId) {
        // we need input to search
        sendSubredditImage(chatId, service.findRandomBySubreddit(DEF_SUBREDDIT));
    }

    @Async
    @Override
    public void handle(long chatId, String query) {
        sendPhoto(chatId, googleSearchService.searchImageNSFW(query));
    }

    @Override
    public String getBotToken() {
        return this.token;
    }
}
