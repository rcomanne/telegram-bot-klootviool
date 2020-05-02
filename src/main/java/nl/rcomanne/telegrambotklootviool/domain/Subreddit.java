package nl.rcomanne.telegrambotklootviool.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "Subreddit")
@Table(name = "subreddit")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Subreddit {
    @Id
    private String name;
    private LocalDateTime lastUpdated;
    private long lowestFromAll;
    private long threshold;

    @OneToMany(
            mappedBy = "subreddit",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SubredditImage> images;

}
