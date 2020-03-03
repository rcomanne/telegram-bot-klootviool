package nl.rcomanne.telegrambotklootviool.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Document(collection = "subreddits")
public class Subreddit {
    @Id
    private String name;
    private LocalDateTime lastUpdated;

}
