package nl.rcomanne.telegrambotklootviool.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "memes")
public class MemeItem {
    @Id
    private String id;
    private String title;
    private boolean animated;
    private boolean nsfw;
    private String imageLink;
}
