package nl.rcomanne.telegrambotklootviool.scraper.domain;

import lombok.Data;

import java.util.List;

@Data
public class ImgurSubredditResponse {
    List<ImgurSubredditResponseItem> data;
}
