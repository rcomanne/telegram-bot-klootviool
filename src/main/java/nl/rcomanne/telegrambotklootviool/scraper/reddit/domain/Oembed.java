package nl.rcomanne.telegrambotklootviool.scraper.reddit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Oembed {
    private String providerUrl;
    private String description;
    private String title;
    private String authorName;
    private int height;
    private int width;
    private String html;
    private int thumbnailWidth;
    private String version;
    private String providerName;
    private String thumbnailUrl;
    private String type;
    private int thumbnailHeight;
}
