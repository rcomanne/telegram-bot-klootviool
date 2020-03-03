package nl.rcomanne.telegrambotklootviool.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "available_insta_accounts")
public class InstaAccount {

    @Id
    private String name;
    private LocalDateTime lastUpdated;

}
