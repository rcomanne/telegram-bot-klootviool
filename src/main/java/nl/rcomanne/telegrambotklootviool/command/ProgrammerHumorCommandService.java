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
public class ProgrammerHumorCommandService extends AbstractCommandService {
    @Value("${bot.token}")
    private String token;

    private static final String SUBREDDIT = "programmerhumor";

    private final SubredditImageService service;

    public void handle(long chatId) {
        SubredditImage selected = service.findRandomBySubreddit(SUBREDDIT);
        if (selected.isAnimated()) {
            sendAnimation(chatId, selected.getImageLink());
        } else {
            sendPhoto(chatId, selected.getImageLink());
        }
    }

    public void handle(long chatId, String query) {
        SubredditImage selected = service.findBySubredditAndTitle(SUBREDDIT, query);
        if (selected.getImageLink().contains(".gif")) {
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
