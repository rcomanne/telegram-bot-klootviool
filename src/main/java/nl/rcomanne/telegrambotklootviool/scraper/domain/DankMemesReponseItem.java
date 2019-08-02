package nl.rcomanne.telegrambotklootviool.scraper.domain;

import java.io.Serializable;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;

import lombok.Data;

@Data
public class DankMemesReponseItem implements Serializable {

    private String link;
    private String description;

    private String gender;
    private int age;

    public SubredditImage toSubredditImage(String subreddit) {
        return SubredditImage.builder()
            .id(resolveId())
            .title(description != null ? description : "no title available")
            .imageLink(link)
            .subreddit(subreddit)
            .animated(link.contains(".gif"))
            // todo: placeholder, might want to actually resolve if it's NSFW or not
            .nsfw(false)
            .build();
    }

    private String resolveId() {
        return link.substring((link.lastIndexOf('/') + 1), link.lastIndexOf('.'));
    }
}
