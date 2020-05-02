package nl.rcomanne.telegrambotklootviool.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
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
    private String title;
    private String imageLink;
    private boolean animated;
    private boolean nsfw;
    private long score;
    private String source;

    @ManyToOne
    private Subreddit subreddit;
}
