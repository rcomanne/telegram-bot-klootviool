package nl.rcomanne.telegrambotklootviool.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.Subreddit;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.MessageService;
import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final String ME_CHAT_ID = "620393195";

    private final MessageService messageService;
    private final SubredditService redditService;

    @Value("#{'${subreddit.banned}'.split(',')}")
    private List<String> bannedSubs;


    @Scheduled(cron = "0 0 4 * * *")
    public void updateSubreddits() {
        log.info("scheduled updating subreddits");
        List<Subreddit> subreddits = redditService.getAllSubreddits();
        HashMap<String, List<String>> updatedSubreddits = new HashMap<>();
        List<Subreddit> notToUpdate = new ArrayList<>();

        for (Subreddit subreddit : subreddits) {
            if (subreddit.getLastUpdated().isBefore(LocalDateTime.now().minusWeeks(1))) {
                log.info("subreddit {} has not been updated for more than one week, updating now", subreddit.getName());
                try {
                    List<SubredditImage> images = redditService.weeklyUpdate(subreddit);
                    if (images.isEmpty()) {
                        log.info("no images found for subreddit {} while updating", subreddit.getName());
                    } else {
                        log.info("updated subreddit {} with {} images", subreddit.getName(), images);
                        ArrayList<String> info = new ArrayList<>();
                        info.add(String.valueOf(images.size()));
                        info.add(images.get(0).getTitle());
                        info.add(images.get(0).getImageLink());
                        updatedSubreddits.put(subreddit.getName(), info);
                    }

                } catch (Exception ex) {
                    log.debug("exception occured while updating subreddit {}", subreddit.getName(), ex);
                    messageService.sendMessage(ME_CHAT_ID, "Exception occurred while updating subreddit " + subreddit.getName() + "\n" + ex.getMessage());
                }
            } else {
                log.debug("subreddit '{}' has been updated in the past week, no need to update", subreddit.getName());
                notToUpdate.add(subreddit);
            }
        }

        StringBuilder html = new StringBuilder();
        html.append("<b><u>Generated daily update report</u></b>\n");
        html.append("<b>Updates</b>\n");
        for (Map.Entry<String, List<String>> entry : updatedSubreddits.entrySet()) {
            List<String> info = entry.getValue();
            html.append(String.format("<i>%s</i>\nHas been updated with <i>%s<i> images\n![%s](%s)\n", entry.getKey(), info.get(0), info.get(1), info.get(2)));
        }

        html.append("<b>Not updated</b>\n");
        for (Subreddit subreddit : notToUpdate) {
            html.append(String.format("<i>%s</i>\n", subreddit.getName()));
        }

        messageService.sendHtmlMessage(ME_CHAT_ID, html.toString());
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanup() {
        log.debug("scheduled cleaning of database...");
        List<Subreddit> subreddits = redditService.getAllSubreddits();

        StringBuilder html = new StringBuilder();

        html.append("<b><u>Generated daily cleanup report</b></u>\n");
        if (!subreddits.isEmpty()) {
            html.append("<b>Removed subreddits</b>\n");
            for (Subreddit subreddit : subreddits) {
                for (String toRemoveName : bannedSubs) {
                    if (toRemoveName.equalsIgnoreCase(subreddit.getName())) {
                        redditService.removeSubreddit(subreddit);
                        html.append(String.format("Removed <i>%s</i> because it is banned\n", subreddit.getName()));
                    }
                }
                if (subreddit.getImages().isEmpty()) {
                    redditService.removeSubreddit(subreddit);
                    html.append(String.format("Removed </i>%s</i> because it is empty\n", subreddit.getName()));
                }
            }
        }

        subreddits = redditService.getAllSubreddits();
        if (!subreddits.isEmpty()) {
            html.append("<b>Available subreddits</b>\n");

            for (Subreddit subreddit : subreddits) {
                html.append(String.format("<i>%s </i>", subreddit.getName()));
                html.append(String.format("contains %d images ", subreddit.getImages().size()));
                html.append(String.format("with a score threshold of %d ", subreddit.getThreshold()));
                html.append(String.format("and last updated at %s\n", subreddit.getLastUpdated().toString()));
            }
        }

        if (!bannedSubs.isEmpty()) {
            html.append("<b>Banned subreddits</b>\n");
            for (String subreddit : bannedSubs) {
                html.append(String.format("<s>%s</s>\n", subreddit));
            }
        }

        messageService.sendHtmlMessage(ME_CHAT_ID, html.toString());
    }
}
