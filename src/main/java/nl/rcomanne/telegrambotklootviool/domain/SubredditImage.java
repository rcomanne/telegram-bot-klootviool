package nl.rcomanne.telegrambotklootviool.domain;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SubredditImage implements Serializable {
    private String id;
    private String title;
    private String subreddit;
    private boolean animated;
    private boolean nsfw;
    private String imageLink;
}
