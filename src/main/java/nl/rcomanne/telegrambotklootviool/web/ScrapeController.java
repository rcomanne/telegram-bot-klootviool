package nl.rcomanne.telegrambotklootviool.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.scraper.ImgurSubredditScraper;
import nl.rcomanne.telegrambotklootviool.service.MemeService;
import nl.rcomanne.telegrambotklootviool.service.ProgrammerHumorService;
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

    private final ImgurSubredditScraper scraper;
    private final ProgrammerHumorService programmerHumorService;
    private final MemeService memeService;

    @GetMapping("/{subreddit}")
    public ResponseEntity scrapeSubreddit(@PathVariable("subreddit") String subreddit) {
        log.debug("scraping subreddit {} for images", subreddit);
        List<SubredditImage> images = scraper.scrapeSubreddit(subreddit);
        log.debug("scraped {} images from {}", images.size(), subreddit);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/ph")
    public ResponseEntity scrapeProgrammerHumorSubreddit() {
        log.debug("scraping memes programmerHumor for images");
        List<SubredditImage> images = scraper.scrapeSubreddit("programmerhumor");
        return ResponseEntity.ok(programmerHumorService.saveFromScraper(images));
    }

    @GetMapping("/memes")
    public ResponseEntity scrapeMemesSubreddit() {
        List<SubredditImage> images = scraper.scrapeSubreddit("memes");
        return ResponseEntity.ok(memeService.saveFromScraper(images));
    }
}
