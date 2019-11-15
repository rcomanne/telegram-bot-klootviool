package nl.rcomanne.telegrambotklootviool.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Document(collection = SubredditImage.COLLECTION_NAME)
public class SubredditImage implements Serializable {
    @Transient
    private static final long serialVersionUID = 5599518219056237194L;

    @Transient
    public static final String COLLECTION_NAME = "images";

    @Id
    private String id;
    private String title;
    private String imageLink;
    private String subreddit;
    private boolean animated;
    private boolean nsfw;
    private long score;
    private String source;
}
