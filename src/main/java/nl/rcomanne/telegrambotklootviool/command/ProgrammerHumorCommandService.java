package nl.rcomanne.telegrambotklootviool.command;

import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.PhItem;
import nl.rcomanne.telegrambotklootviool.service.ProgrammerHumorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProgrammerHumorCommandService extends AbstractCommandService {
    @Value("${bot.token}")
    private String token;

    private final ProgrammerHumorService service;

    @Autowired
    public ProgrammerHumorCommandService(ProgrammerHumorService service) {
        this.service = service;
    }

    public void handle(long chatId) {
        PhItem selected = service.getRandomPhItem();
        if (selected.isAnimated()) {
            sendAnimation(chatId, selected.getImageLink());
        } else {
            sendPhoto(chatId, selected.getImageLink());
        }
    }

    public void handle(long chatId, String query) {
        // todo: make search available when using titles or sum
        PhItem selected = service.getRandomPhItem();
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
