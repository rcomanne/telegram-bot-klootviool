package nl.rcomanne.telegrambotklootviool.command;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.SubredditImageService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemeCommandService extends AbstractCommandService {
    @Value("${bot.token}")
    private String token;

    private static final String SUBREDDIT_NAME = "memes";

    private final SubredditImageService service;

    @Override
    public void handle(long chatId) {
        SubredditImage selected = service.findRandomBySubreddit(SUBREDDIT_NAME);
        if (selected.isAnimated()) {
            sendAnimation(chatId, selected.getImageLink());
        } else {
            sendPhoto(chatId, selected.getImageLink());
        }
    }

    @Override
    public void handle(long chatId, String query) {
        SubredditImage selected = service.findBySubredditAndTitle(SUBREDDIT_NAME, query);
        if (selected.isAnimated()) {
            sendAnimation(chatId, selected.getImageLink());
        } else {
            sendPhoto(chatId, selected.getImageLink());
        }
    }

    @Override
    public String getBotToken() {
        return this.token;
    }
}
