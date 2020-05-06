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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
            if (subreddit.getLastUpdated() == null) {
                subreddit.setLastUpdated(LocalDateTime.now().minusYears(1));
            }
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

        StringBuilder markdown = new StringBuilder();
        markdown.append("# Generated daily update report\n");
        markdown.append("## Updates\n");
        for (Map.Entry<String, List<String>> entry : updatedSubreddits.entrySet()) {
            List<String> info = entry.getValue();
            markdown.append(String.format("### %s\nHas been updated with %s images\n![%s](%s)\n", entry.getKey(), info.get(0), info.get(1), info.get(2)));
        }
        markdown.append("# Not updated\n");
        for (Subreddit subreddit : notToUpdate) {
            markdown.append(String.format("### %s\n", subreddit.getName()));
        }

        messageService.sendMarkdownMessage(ME_CHAT_ID, markdown.toString());
    }

    @Transactional
    @Scheduled(cron = "0 0 2 * * *")
    public void cleanup() {
        log.debug("scheduled cleaning of database...");
        List<Subreddit> subreddits = redditService.getAllSubreddits();

        StringBuilder markdown = new StringBuilder();
        markdown.append("# Generated daily cleanup report\n");
        markdown.append("## Removed subreddits\n");
        for (Subreddit subreddit : subreddits) {
            for (String toRemoveName : bannedSubs) {
                if (toRemoveName.equalsIgnoreCase(subreddit.getName())) {
                    redditService.removeSubreddit(subreddit);
                    markdown.append(String.format("- Removed %s because it is banned\n", subreddit.getName()));
                }
            }
            if (subreddit.getImages().isEmpty()) {
                redditService.removeSubreddit(subreddit);
                markdown.append(String.format("- Removed %s because it is empty\n", subreddit.getName()));
            }
        }
        subreddits = redditService.getAllSubreddits();
        markdown.append("\n## Available subreddits\n");

        if (!subreddits.isEmpty()) {
            markdown.append("\n| Name | Images | Threshold | Last Updated |\n");
            markdown.append("| --- | --- | --- | --- |\n");
            for (Subreddit subreddit : subreddits) {
                markdown.append(String.format("| %s ", subreddit.getName()));
                markdown.append(String.format("| %d ", subreddit.getImages().size()));
                markdown.append(String.format("| %d ", subreddit.getThreshold()));
                markdown.append(String.format("| %s |\n", subreddit.getLastUpdated().toString()));
            }
        }

        markdown.append("\n## Banned subreddits\n");
        for (String subreddit : bannedSubs) {
            markdown.append(String.format("- %s\n", subreddit));
        }

        messageService.sendMarkdownMessage(ME_CHAT_ID, markdown.toString());
    }
}
