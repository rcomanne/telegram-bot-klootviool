package nl.rcomanne.telegrambotklootviool.scheduled;

import java.util.List;
import java.util.Random;

import nl.rcomanne.telegrambotklootviool.domain.Subreddit;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.MessageService;
import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final String CHAT_ID = "-320932775";
    private static final String ME_CHAT_ID = "620393195";
    private static final String DEFAULT_WINDOW = "day";

    private final Random random = new Random();

    private final MessageService messageService;
    private final SubredditService redditService;


    @Scheduled(cron = "0 0 4 7 * *")
    public void updateSubreddits() {
        log.info("scheduled updating subreddits");
        List<Subreddit> subreddits = redditService.getAllSubreddits();

        for (Subreddit subreddit : subreddits) {
            List<SubredditImage> images = redditService.weeklyUpdate(subreddit);
            if (images.isEmpty()) {
                log.info("no images found for subreddit {} while updating", subreddit);
                messageService.sendMessageRandomPhoto(ME_CHAT_ID, String.format("no images found - or no scrape executed - for subreddit %s", subreddit));
            } else {
                log.info("updated subreddit {} with {} images", subreddit, images);
                final int size = images.size();
                messageService.sendMessageWithPhoto(ME_CHAT_ID, String.format("found %d images for subreddit %s", size, subreddit), images.get(random.nextInt(size - 1)));
            }
        }
    }
}
