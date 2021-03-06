package nl.rcomanne.telegrambotklootviool.scraper.reddit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Child {

    private ChildData data;

    private String kind;

    public String getSource() {
        if (data != null && data.getUrl() != null) {
            if (data.getMedia() != null && data.getMedia().getType() != null) {
                return data.getMedia().getType();
            }
            if (data.getUrl().contains("gfycat")) {
                return "gfycat.com";
            }
            if (data.getUrl().contains("redd.it") || data.getUrl().contains("reddit")) {
                return "reddit";
            }
            if (data.getUrl().contains("imgur")) {
                return "imgur";
            }
        } return "unknown";
    }

    public boolean isAnimated() {
        if (data != null && data.getUrl() != null) {
            if (data.getUrl().contains("gif")) {
                return true;
            }
            if (data.getMedia() != null && data.getMedia().getOembed() != null) {
                String type = data.getMedia().getOembed().getType();
                return type.equalsIgnoreCase("video") || type.equalsIgnoreCase("gif");
            }
        }
        return false;
    }
}
