package nl.rcomanne.telegrambotklootviool.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.MemeItem;
import nl.rcomanne.telegrambotklootviool.service.MemeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemeCommandService extends AbstractCommandService {
    @Value("${bot.token}")
    private String token;

    private final MemeService memeService;

    @Override
    void handle(long chatId) {
        MemeItem selected = memeService.getRandom();
        if (selected.isAnimated()) {
            sendAnimation(chatId, selected.getImageLink());
        } else {
            sendPhoto(chatId, selected.getImageLink());
        }
    }

    @Override
    void handle(long chatId, String query) {
        // handle
    }

    @Override
    public String getBotToken() {
        return this.token;
    }
}
