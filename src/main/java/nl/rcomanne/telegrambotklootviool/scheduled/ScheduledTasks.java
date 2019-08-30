package nl.rcomanne.telegrambotklootviool.scheduled;

import java.util.List;
import java.util.Random;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.MessageService;
import nl.rcomanne.telegrambotklootviool.service.SubredditImageService;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("#{'${subreddit.list}'.split(',')}")
    private List<String> subreddits;

    private final Random random = new Random();

    private final MessageService messageService;
    private final SubredditImageService imageService;

/*
    @Scheduled(cron = "0 0 * * * *")
    public void testUpdateTime() {
        log.info("testing update timing for imgur");
        List<SubredditImage> images = imageService.scrapeAndSave("me_irl", DEFAULT_WINDOW);
        if (images.isEmpty()) {
            log.info("no images found with test update");
            messageService.sendRandomPhoto(ME_CHAT_ID);
        } else {
            final int size = images.size();
            messageService.sendMessageWithPhoto(ME_CHAT_ID, String.format("found %d images for me_irl", size), images.get(random.nextInt(size - 1)));
        }
    }
*/

    @Scheduled(cron = "0 0 8/2 * * *")
    public void sendRandomPhoto() {
        log.info("sending random photo");
        messageService.sendRandomPhoto(CHAT_ID);
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void updateSubreddits() {
        log.info("scheduled updating subreddits");
        for (String subreddit : subreddits) {
            List<SubredditImage> images = imageService.scrapeAndSave(subreddit, DEFAULT_WINDOW);
            if (images.isEmpty()) {
                log.info("no images found for subreddit {} while updating", subreddit);
                messageService.sendMessageRandomPhoto(ME_CHAT_ID, String.format("no images found for subreddit %s", subreddit));
            } else {
                log.info("updated subreddit {} with {} images", subreddit, images);
                final int size = images.size();
                messageService.sendMessageWithPhoto(ME_CHAT_ID, String.format("found %d images for subreddit %s", size, subreddit), images.get(random.nextInt(size - 1)));
            }
        }
    }
}
