package nl.rcomanne.telegrambotklootviool.command;

import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.Quote;
import nl.rcomanne.telegrambotklootviool.service.QuoteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QuoteCommandService extends AbstractCommandService {
    @Value("${bot.token}")
    private String token;

    private final QuoteService quoteService;

    public QuoteCommandService(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @Override
    void handle(long chatId) {
       Quote selected = quoteService.getRandomQuote();
       sendAnimationWithCaption(chatId, selected.getImgLink(), selected.getMessage());
    }

    @Override
    void handle(long chatId, String query) {
        Quote selected = quoteService.findByMessage(query);
        sendAnimationWithCaption(chatId, selected.getImgLink(), selected.getMessage());
    }

    @Override
    public String getBotToken() {
        return this.token;
    }
}
