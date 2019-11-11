package nl.rcomanne.telegrambotklootviool.scraper.reddit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Media {
    private Oembed oembed;
    private String type;
}
