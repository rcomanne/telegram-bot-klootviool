package nl.rcomanne.telegrambotklootviool.web.dto;

import lombok.Data;
import nl.rcomanne.telegrambotklootviool.domain.Quote;

import java.util.UUID;

@Data
public class QuoteDto {
    private String message;
    private String imageLink;

    public Quote toQuote() {
        return Quote.builder()
                .id(UUID.randomUUID().toString())
                .message(this.message)
                .imgLink(this.imageLink)
                .build();
    }
}
