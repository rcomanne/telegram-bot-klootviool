package nl.rcomanne.telegrambotklootviool.command;

import java.util.List;
import java.util.Random;

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
public class UpdateCommandService extends AbstractCommandService {
    @Value("${bot.token}")
    private String token;

    private static final String DEF_WINDOW = "day";

    private final SubredditService service;

    private final Random r = new Random();

    @Override
    public void handle(long chatId) {
        sendMessage(chatId, "we need a subreddit name to update!");
    }

    @Async
    @Override
    public void handle(long chatId, String query) {
        List<SubredditImage> images = service.scrapeAndSave(query, DEF_WINDOW);
        if (images.isEmpty()) {
            sendMessage(chatId, "no images found for subreddit " + query);
        } else {
            SubredditImage selected = images.get(r.nextInt(images.size()));
            sendSubredditImage(chatId, selected);
        }
    }

    @Override
    public String getBotToken() {
        return this.token;
    }
}
