package nl.rcomanne.telegrambotklootviool.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "programmerhumor")
public class PhItem {
    @Id
    private String id;
    private String title;
    private boolean animated;
    private boolean nsfw;
    private String imageLink;
}
