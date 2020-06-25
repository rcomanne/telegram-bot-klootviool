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

    @Transactional
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
                        log.info("updated subreddit {} with {} images", subreddit.getName(), images.size());
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
        markdown.append("*__Generated daily update report__*\n");
        markdown.append("*Updated*\n");
        StringBuilder html = new StringBuilder();
        html.append("<b><u>Generated daily update report</u></b>\n");
        html.append("<b>Updated</b>\n");
        for (Map.Entry<String, List<String>> entry : updatedSubreddits.entrySet()) {
            List<String> info = entry.getValue();
            markdown.append(String.format("_%s_ has been updated with _%s_ images!\n[%s][%s)\n", entry.getKey(), info.get(0), info.get(1), info.get(2)));
            html.append(String.format("<i>%s</i>\nHas been updated with <i>%s<i> images\n![%s](%s)\n", entry.getKey(), info.get(0), info.get(1), info.get(2)));
        }

        markdown.append("*Not updated*\n");
        html.append("<b>Not updated</b>\n");
        for (Subreddit subreddit : notToUpdate) {
            markdown.append(String.format("_%s_\n", subreddit.getName()));
            html.append(String.format("<i>%s</i>\n", subreddit.getName()));
        }

        messageService.sendMarkdownMessage(ME_CHAT_ID, markdown.toString());
//        messageService.sendHtmlMessage(ME_CHAT_ID, html.toString());
    }

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void cleanup() {
        log.debug("scheduled cleaning of database...");
        List<Subreddit> subreddits = redditService.getAllSubreddits();

        StringBuilder html = new StringBuilder();
        StringBuilder markdown = new StringBuilder();

        markdown.append("*__Generated daily cleanup report__*\n");
        html.append("<b><u>Generated daily cleanup report</b></u>\n");
        if (!subreddits.isEmpty()) {
            markdown.append("*Removed subreddits*\n");
            html.append("<b>Removed subreddits</b>\n");
            for (Subreddit subreddit : subreddits) {
                for (String toRemoveName : bannedSubs) {
                    if (toRemoveName.equalsIgnoreCase(subreddit.getName())) {
                        redditService.removeSubreddit(subreddit);
                        markdown.append(String.format("Removed _%s_ because it is banned\n", subreddit.getName()));
                        html.append(String.format("Removed <i>%s</i> because it is banned\n", subreddit.getName()));
                    }
                }
                if (subreddit.getImages().isEmpty()) {
                    redditService.removeSubreddit(subreddit);
                    markdown.append(String.format("Removed _%s_ because it is empty\n", subreddit.getName()));
                    html.append(String.format("Removed </i>%s</i> because it is empty\n", subreddit.getName()));
                }
            }
        }

        if (!subreddits.isEmpty()) {
            markdown.append("*Available subreddits*\n");
            html.append("<b>Available subreddits</b>\n");

            for (Subreddit subreddit : subreddits) {
                markdown.append(String.format("_%s_ \n", subreddit.getName()));
                markdown.append(String.format("contains %d images\n", subreddit.getImages().size()));
                markdown.append(String.format("with a score threshold of %d\n", subreddit.getThreshold()));
                markdown.append(String.format("and last updated at %s \n\n", subreddit.getLastUpdated().toString()));
                html.append(String.format("<i>%s </i>\n", subreddit.getName()));
                html.append(String.format("contains %d images\n", subreddit.getImages().size()));
                html.append(String.format("with a score threshold of %d\n", subreddit.getThreshold()));
                html.append(String.format("and last updated at %s\n\n", subreddit.getLastUpdated().toString()));
            }
        }

        if (!bannedSubs.isEmpty()) {
            markdown.append("*Banned subreddits*\n");
            html.append("<b>Banned subreddits</b>\n");
            for (String subreddit : bannedSubs) {
                markdown.append(String.format("~%s~", subreddit));
                html.append(String.format("<s>%s</s>\n", subreddit));
            }
        }

        messageService.sendMarkdownMessage(ME_CHAT_ID, markdown.toString());
//        messageService.sendHtmlMessage(ME_CHAT_ID, html.toString());
    }
}
