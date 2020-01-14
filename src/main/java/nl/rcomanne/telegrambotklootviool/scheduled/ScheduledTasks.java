package nl.rcomanne.telegrambotklootviool.scheduled;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import nl.rcomanne.telegrambotklootviool.domain.Subreddit;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.MessageService;
import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final String ME_CHAT_ID = "620393195";

    private final Random random = new Random();

    private final MessageService messageService;
    private final SubredditService redditService;

    @Value("#{'${subreddit.toremove}'.split(',')}")
    private List<String> toRemove;


    @Scheduled(cron = "0 0 4 * * *")
    public void updateSubreddits() {
        log.info("scheduled updating subreddits");
        List<Subreddit> subreddits = redditService.getAllSubreddits();

        for (Subreddit subreddit : subreddits) {
            List<SubredditImage> images = redditService.weeklyUpdate(subreddit);
            if (images.isEmpty()) {
                log.info("no images found for subreddit {} while updating", subreddit);
                messageService.sendMessageRandomPhoto(ME_CHAT_ID, String.format("no images found - or no scrape executed - for subreddit '%s', last updated '%s'", subreddit.getName(), subreddit.getLastUpdated()), subreddit);
            } else {
                log.info("updated subreddit {} with {} images", subreddit, images);
                final int size = images.size();
                messageService.sendMessageWithPhoto(ME_CHAT_ID, String.format("found %d images for subreddit %s", size, subreddit), images.get(random.nextInt(size - 1)));
            }
        }
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanup() {
        log.debug("scheduled cleaning of database...");
        List<Subreddit> subreddits = redditService.getAllSubreddits();

        for (Subreddit subreddit : subreddits) {
            redditService.removeSubredditIfEmpty(subreddit);
            for (String toRemoveName : toRemove) {
                if (toRemoveName.equalsIgnoreCase(subreddit.getName())) {
                    redditService.removeSubreddit(subreddit);
                }
            }
        }
        subreddits = redditService.getAllSubreddits();
        messageService.sendMessageRandomPhoto(ME_CHAT_ID, subreddits.stream().map(Subreddit::getName).collect(Collectors.joining(",")));
    }
}
