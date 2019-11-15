package nl.rcomanne.telegrambotklootviool.scraper.reddit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Child {

    private String kind;
    private ChildData data;

    public boolean isAnimated() {
        boolean animated = false;

        if (data != null && data.getMedia() != null) {
            if (data.getMedia().getOembed().getType().equalsIgnoreCase("video"))
                animated = true;
        }

        return animated;
    }
}
