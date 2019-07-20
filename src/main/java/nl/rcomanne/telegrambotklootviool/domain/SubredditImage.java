package nl.rcomanne.telegrambotklootviool.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

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
}
