package nl.rcomanne.telegrambotklootviool.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/scrape")
@RequiredArgsConstructor
public class ScrapeController {

    private final SubredditService subredditService;

    @GetMapping("/{subreddit}/{startPage}")
    public ResponseEntity<List<SubredditImage>> scrapeSubredditDefault(@PathVariable("subreddit") String subreddit, @PathVariable("startPage") String startPage) {
        log.info("scraping subreddit {} for images", subreddit);
        return ResponseEntity.ok(subredditService.scrapeAndSaveAllTime(subreddit));
    }
}
