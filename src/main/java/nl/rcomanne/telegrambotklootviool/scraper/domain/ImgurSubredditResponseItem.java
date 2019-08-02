package nl.rcomanne.telegrambotklootviool.scraper.domain;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = {"id", "title", "link", "section"})
public class ImgurSubredditResponseItem {
    private String id;
    private String title;
    private String description;
    private long datetime;
    private String type;
    private boolean animated;
    private long width;
    private long height;
    private long size;
    private long views;
    private long bandwidth;
    private String vote;
    private boolean favorite;
    private boolean nsfw;
    private String section;
    private String accountUrl;
    private String accountId;
    private boolean isAd;
    private boolean inMostViral;
    private boolean hasSound;
    private List<String> tags;
    private long adType;
    private String adUrl;
    private long edited;
    private boolean inGallery;
    private String link;
    private AdConfig adConfig;
    private long commentCount;
    private long favoriteCount;
    private long ups;
    private long downs;
    private long polongs;
    private long score;
    private boolean isAlbum;
}
