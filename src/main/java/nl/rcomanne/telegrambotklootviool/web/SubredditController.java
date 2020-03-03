package nl.rcomanne.telegrambotklootviool.web;

import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/r")
@RequiredArgsConstructor
public class SubredditController {

    private final SubredditService imageService;

    @GetMapping("/reset/{subreddit}")
    public ResponseEntity<String> resetLastUpdated(@PathVariable("subreddit") String subreddit) {
        log.info("resetting last updated for subreddit '{}'", subreddit);
        imageService.resetLastUpdatedForSubreddit(subreddit);
        return ResponseEntity.ok(subreddit);
    }
}
