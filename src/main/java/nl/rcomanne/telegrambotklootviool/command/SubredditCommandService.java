package nl.rcomanne.telegrambotklootviool.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.service.SubredditImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubredditCommandService extends AbstractCommandService {
    @Value("${bot.token}")
    private String token;

    private final SubredditImageService service;

    @Override
    public void handle(long chatId) {
        sendSubredditImage(chatId, service.findRandom());
    }

    @Override
    public void handle(long chatId, String query) {
        sendSubredditImage(chatId, service.findRandomBySubreddit(query));
    }

    @Override
    public String getBotToken() {
        return this.token;
    }
}
