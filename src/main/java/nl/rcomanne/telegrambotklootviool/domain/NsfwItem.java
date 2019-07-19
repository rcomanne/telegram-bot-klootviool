package nl.rcomanne.telegrambotklootviool.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "nsfw")
public class NsfwItem {
    @Id
    private String id;
    private String imgLink;
}
