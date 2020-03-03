package nl.rcomanne.telegrambotklootviool.scraper;

import java.util.List;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;

public interface SubredditScraper {
    List<SubredditImage> scrapeSubreddit(String subreddit, String window);

}
