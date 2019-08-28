package nl.rcomanne.telegrambotklootviool.scheduled;

import java.util.List;
import java.util.Random;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.MessageService;
import nl.rcomanne.telegrambotklootviool.service.SubredditImageService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final String CHAT_ID = "-320932775";
    private static final String DEFAULT_WINDOW = "day";

    private final Random random = new Random();

    private final MessageService messageService;
    private final SubredditImageService imageService;

    @Scheduled(cron = "0 0 8-24 * * *")
    public void sendRandomPhoto() {
        log.info("sending random photo");
        messageService.sendRandomPhoto(CHAT_ID);
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void updateSubreddits() {
        log.info("scheduled updating subreddits");
        List<String> subreddits = List.of("memes", "programmerhumor", "gonewild", "vsmodels", "realgirls", "biggerthanyouthought", "ik_ihe", "me_irl");
        for (String subreddit : subreddits) {
            List<SubredditImage> images = imageService.scrapeAndSave(subreddit, DEFAULT_WINDOW);
            if (images.isEmpty()) {
                log.info("no images found for subreddit {} while updating", subreddit);
                messageService.sendMessageRandomPhoto(CHAT_ID, String.format("no images found for subreddit %s", subreddit));
            } else {
                log.info("updated subreddit {} with {} images", subreddit, images);
                final int size = images.size();
                messageService.sendMessageWithPhoto(CHAT_ID, String.format("found %d images for subreddit %s", size, subreddit), images.get(random.nextInt(1 + size)));
            }
        }
    }
}
