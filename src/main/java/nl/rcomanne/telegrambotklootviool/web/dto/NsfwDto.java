package nl.rcomanne.telegrambotklootviool.web.dto;

import java.util.UUID;

import nl.rcomanne.telegrambotklootviool.domain.NsfwItem;

import lombok.Data;

@Data
public class NsfwDto {
    private String imageLink;

    public NsfwItem toNsfwItem() {
        return NsfwItem.builder()
                .id(UUID.randomUUID().toString())
                .imgLink(this.imageLink)
                .build();
    }
}
