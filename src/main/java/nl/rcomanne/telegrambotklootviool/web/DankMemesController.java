package nl.rcomanne.telegrambotklootviool.web;

import java.util.List;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.scraper.DankMemesScraper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/dank")
public class DankMemesController {

    private final DankMemesScraper scraper;

    @GetMapping("/{subreddit}")
    public ResponseEntity<List<SubredditImage>> getDank(@PathVariable("subreddit") String subreddit) {
        return ResponseEntity.ok(scraper.scrapeDankMemes(subreddit));
    }

}
