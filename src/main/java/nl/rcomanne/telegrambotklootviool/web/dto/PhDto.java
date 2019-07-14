package nl.rcomanne.telegrambotklootviool.web.dto;

import lombok.Data;
import nl.rcomanne.telegrambotklootviool.domain.PhItem;

import java.util.UUID;

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
