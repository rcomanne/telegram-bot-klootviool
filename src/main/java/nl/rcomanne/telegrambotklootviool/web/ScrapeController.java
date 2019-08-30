package nl.rcomanne.telegrambotklootviool.web;

import java.util.List;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.SubredditImageService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/scrape")
@RequiredArgsConstructor
public class ScrapeController {

    private final SubredditImageService imageService;

    @GetMapping("/{subreddit}/{startPage}")
    public ResponseEntity<List<SubredditImage>> scrapeSubredditDefault(@PathVariable("subreddit") String subreddit, @PathVariable("startPage") int startPage) {
        log.info("scraping subreddit {} for images", subreddit);
        return ResponseEntity.ok(imageService.scrapeAndSaveAllTime(subreddit, startPage));
    }

    @GetMapping("{subreddit}")
    public ResponseEntity<List<SubredditImage>> scrapeTop(@PathVariable("subreddit") String subreddit) {
        log.info("scraping top for {}", subreddit);
        return ResponseEntity.ok(imageService.scrapeAndSaveTop(subreddit, 0 ,100));
    }
}
