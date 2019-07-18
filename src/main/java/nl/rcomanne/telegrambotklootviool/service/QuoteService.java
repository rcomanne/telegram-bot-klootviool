package nl.rcomanne.telegrambotklootviool.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.Quote;
import nl.rcomanne.telegrambotklootviool.repositories.QuoteRepository;
import nl.rcomanne.telegrambotklootviool.web.dto.QuoteDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuoteService {
    private final QuoteRepository repository;
    private final Random r = new Random();

    public Quote findByMessage(String message) {
        log.debug("finding quote by message {}", message);
        return repository.findByMessageContains(message).orElse(getRandomQuote());
    }

    public Quote getRandomQuote() {
        log.debug("finding random quote");
        List<Quote> quotes = repository.findAll();
        if (quotes.isEmpty()) {
            throw new IllegalStateException("no quotes available");
        }
        return quotes.get(r.nextInt(quotes.size()));
    }

    public String save(QuoteDto dto) {
        log.debug("saving quote: {}", dto.toString());
        return repository.save(dto.toQuote()).getId();
    }

    public List<String> save(List<QuoteDto> quotes) {
        log.debug("saving {} quotes", quotes.size());
        List<String> ids = new ArrayList<>();
        for (QuoteDto dto : quotes) {
            ids.add(save(dto));
        }
        return ids;
    }
}
