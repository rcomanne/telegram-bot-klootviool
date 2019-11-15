package nl.rcomanne.telegrambotklootviool.command;

import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubredditCommandService extends AbstractCommandService {
    @Value("${bot.token}")
    private String token;

    private final SubredditService service;

    @Override
    public void handle(long chatId) {
        sendSubredditImage(chatId, service.findRandom());
    }

    @Async
    @Override
    public void handle(long chatId, String query) {
        sendSubredditImage(chatId, service.findRandomBySubreddit(query));
    }

    @Override
    public String getBotToken() {
        return this.token;
    }
}
