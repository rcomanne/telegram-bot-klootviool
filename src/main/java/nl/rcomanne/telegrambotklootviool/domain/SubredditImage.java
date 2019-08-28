package nl.rcomanne.telegrambotklootviool.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Document(collection = "subreddit")
public class SubredditImage implements Serializable {
    @Transient
    private static final String NSFW_REGEX = "/(\\(|\\[)(\\s+)?m(ale)?(\\s+)?(\\)|\\])/gi";

    private String id;
    private String title;
    private String subreddit;
    private boolean animated;
    private boolean nsfw;
    private String imageLink;
    private long score;

    public boolean isMale() {
        return (title.matches(NSFW_REGEX));
    }
}
