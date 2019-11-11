package nl.rcomanne.telegrambotklootviool.scraper.reddit.domain;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Preview {

    private List<Image> images;

    private boolean enabled;
}
