package nl.rcomanne.telegrambotklootviool.web.dto;

import lombok.Data;
import nl.rcomanne.telegrambotklootviool.domain.NsfwItem;

import java.util.UUID;

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
