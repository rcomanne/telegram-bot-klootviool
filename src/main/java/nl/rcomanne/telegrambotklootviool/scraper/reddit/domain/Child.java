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
        if (data != null && data.getMedia() != null && data.getMedia().getOembed() != null) {
            if (data.getUrl().contains(".gif")) {
                return true;
            }

            if (data.getMedia() != null && data.getMedia().getOembed() != null) {
                String type = data.getMedia().getOembed().getType();
                if (type.equalsIgnoreCase("video") || type.equalsIgnoreCase("gif"))  {
                    return true;
                }
            }
        }
        return false;
    }

    public String getSource() {
        if (data != null && data.getMedia() != null && data.getMedia().getType() != null) {
            return data.getMedia().getType();
        } else {
            return "unknown";
        }
    }
}
