package nl.rcomanne.telegrambotklootviool.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "SubredditImage")
@Table(name = "subreddit_image")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class SubredditImage implements Serializable {
    @Transient
    private static final long serialVersionUID = 5599518219056237194L;

    @Id
    private String id;
    @Lob
    private String title;
    private String imageLink;
    private boolean animated;
    private boolean nsfw;
    private long score;
    @Lob
    private String source;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subreddit subreddit;
}
