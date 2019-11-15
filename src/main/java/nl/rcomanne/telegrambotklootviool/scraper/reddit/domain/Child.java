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

        if (data != null && data.getMedia() != null && data.getMedia().getOembed() != null) {
            if (data.getMedia().getOembed().getType().equalsIgnoreCase("video"))  {
                animated = true;
            }
            if (data.getMedia().getOembed().getType().equalsIgnoreCase("gif")) {
                animated = true;
            }
        }
        return animated;
    }

    public String getSource() {
        if (data != null && data.getMedia() != null && data.getMedia().getType() != null) {
            return data.getMedia().getType();
        } else {
            return "unknown";
        }
    }
}
