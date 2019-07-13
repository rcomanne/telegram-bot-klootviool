package nl.rcomanne.telegrambotklootviool.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "programmerhumor")
public class PhItem {
    @Id
    private String id;
    private String imgLink;
}
