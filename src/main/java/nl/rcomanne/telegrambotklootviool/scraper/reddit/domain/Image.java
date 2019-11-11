package nl.rcomanne.telegrambotklootviool.scraper.reddit.domain;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Image {

    private Source source;
    private List<Resolution> resolutions;
    private Variants variants;
    private String id;
}
