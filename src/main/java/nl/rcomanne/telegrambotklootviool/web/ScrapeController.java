package nl.rcomanne.telegrambotklootviool.web;

import java.util.List;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.scraper.ImgurSubredditScraper;
import nl.rcomanne.telegrambotklootviool.service.MemeService;
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
    private static final String DEF_WINDOW = "day";

    private final ImgurSubredditScraper scraper;
    private final MemeService memeService;
    private final SubredditImageService imageService;

    @GetMapping("/{subreddit}/{window}")
    public ResponseEntity scrapeSubreddit(@PathVariable("subreddit") String subreddit, @PathVariable("window") String window) {
        log.debug("scraping subreddit {} for images", subreddit);
        return ResponseEntity.ok(imageService.scrapeAndSave(subreddit, window));
    }

    @GetMapping("/{subreddit}")
    public ResponseEntity scrapeSubredditDefault(@PathVariable("subreddit") String subreddit) {
        log.debug("scraping subreddit {} for images", subreddit);
        return ResponseEntity.ok(imageService.scrapeAndSave(subreddit, DEF_WINDOW));
    }


    @GetMapping("/memes")
    public ResponseEntity scrapeMemesSubreddit() {
        List<SubredditImage> images = scraper.scrapeSubreddit("memes", DEF_WINDOW);
        return ResponseEntity.ok(memeService.saveFromScraper(images));
    }
}
