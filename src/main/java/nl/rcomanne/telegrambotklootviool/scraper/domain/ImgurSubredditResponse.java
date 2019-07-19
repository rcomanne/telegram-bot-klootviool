package nl.rcomanne.telegrambotklootviool.scraper.domain;

import java.util.List;

import lombok.Data;

@Data
public class ImgurSubredditResponse {
    List<ImgurSubredditResponseItem> data;
}
