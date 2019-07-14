package nl.rcomanne.telegrambotklootviool.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SubredditImage {
    private String id;
    private String title;
    private boolean animated;
    private boolean nsfw;
    private String imageLink;
}
