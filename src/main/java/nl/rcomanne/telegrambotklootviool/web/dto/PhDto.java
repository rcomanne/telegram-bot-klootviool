package nl.rcomanne.telegrambotklootviool.web.dto;

import java.util.UUID;

import nl.rcomanne.telegrambotklootviool.domain.PhItem;

import lombok.Data;

@Data
public class PhDto {
    private String imageLink;

    public PhItem toPhItem() {
        return PhItem.builder()
                .id(UUID.randomUUID().toString())
                .imageLink(this.imageLink)
                .build();
    }
}
