package nl.rcomanne.telegrambotklootviool.domain;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Document(collection = "subreddit")
public class SubredditImage implements Serializable {

    private String id;
    private String title;
    private String subreddit;
    private boolean animated;
    private boolean nsfw;
    private String imageLink;
    private long score;
}
