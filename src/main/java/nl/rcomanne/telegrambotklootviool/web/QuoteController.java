package nl.rcomanne.telegrambotklootviool.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.service.QuoteService;
import nl.rcomanne.telegrambotklootviool.web.dto.QuoteDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/quote")
@RequiredArgsConstructor
public class QuoteController {
    private final QuoteService quoteService;

    @PostMapping
    public ResponseEntity<String> postQuote(@RequestBody QuoteDto dto) {
        log.debug("quote posted, saving");
        return ResponseEntity.ok(quoteService.save(dto));
    }

    @PostMapping("/list")
    public ResponseEntity<List<String>> postQuotes(@RequestBody List<QuoteDto> quotes) {
        log.debug("saving list of quotes");
        return ResponseEntity.ok(quoteService.save(quotes));
    }
}
