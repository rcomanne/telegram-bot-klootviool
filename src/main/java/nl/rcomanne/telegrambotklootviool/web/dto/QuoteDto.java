package nl.rcomanne.telegrambotklootviool.web.dto;

import java.util.UUID;

import nl.rcomanne.telegrambotklootviool.domain.Quote;

import lombok.Data;

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
