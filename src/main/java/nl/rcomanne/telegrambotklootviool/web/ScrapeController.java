package nl.rcomanne.telegrambotklootviool.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.SubredditImageService;
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
    private static final String DEF_WINDOW = "day";

    private final SubredditImageService imageService;

    @GetMapping("/{subreddit}/{window}")
    public ResponseEntity<List<SubredditImage>> scrapeSubreddit(@PathVariable("subreddit") String subreddit, @PathVariable("window") String window) {
        log.info("scraping subreddit {} for images", subreddit);
        return ResponseEntity.ok(imageService.scrapeAndSave(subreddit, window));
    }

    @GetMapping("/{subreddit}")
    public ResponseEntity<List<SubredditImage>> scrapeSubredditDefault(@PathVariable("subreddit") String subreddit) {
        log.info("scraping subreddit {} for images", subreddit);
        return ResponseEntity.ok(imageService.scrapeAndSave(subreddit, DEF_WINDOW));
    }
}
