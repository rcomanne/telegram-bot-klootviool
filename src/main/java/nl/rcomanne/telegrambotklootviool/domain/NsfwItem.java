package nl.rcomanne.telegrambotklootviool.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "nsfw")
public class NsfwItem {
    @Id
    private String id;
    private String imgLink;
}
