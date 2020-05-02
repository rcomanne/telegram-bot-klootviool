package nl.rcomanne.telegrambotklootviool.scraper;

import nl.rcomanne.telegrambotklootviool.domain.Subreddit;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;

import java.util.List;

public interface SubredditScraper {
    List<SubredditImage> scrapeSubreddit(Subreddit subreddit, String window);

}
