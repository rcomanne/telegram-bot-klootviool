package nl.rcomanne.telegrambotklootviool.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.service.GoogleSearchService;
import nl.rcomanne.telegrambotklootviool.service.MemeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PicCommandService extends AbstractCommandService {
    @Value("${bot.token}")
    private String token;

    private final GoogleSearchService googleSearchService;
    private final MemeService memeService;

    @Override
    void handle(long chatId) {
        // we need input to search
        sendPhotoWithCaption(chatId, memeService.getRandom().getImageLink(), "we need a query to search images!");
    }

    @Override
    void handle(long chatId, String query) {
        sendPhoto(chatId, googleSearchService.searchImageNSFW(query));
    }

    @Override
    public String getBotToken() {
        return this.token;
    }
}
