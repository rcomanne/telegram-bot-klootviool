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

    void handle(long chatId) {
        PhItem selected = service.getRandomPhItem();
        if (selected.getImgLink().contains(".gif")) {
            sendAnimation(chatId, selected.getImgLink());
        } else {
            sendPhoto(chatId, selected.getImgLink());
        }
    }

    void handleWithQuery(long chatId, String query) {
        // todo: make search available when using titles or sum
        PhItem selected = service.getRandomPhItem();
        if (selected.getImgLink().contains(".gif")) {
            sendAnimation(chatId, selected.getImgLink());
        } else {
            sendPhoto(chatId, selected.getImgLink());
        }
    }

    @Override
    public String getBotToken() {
        return this.token;
    }
}
