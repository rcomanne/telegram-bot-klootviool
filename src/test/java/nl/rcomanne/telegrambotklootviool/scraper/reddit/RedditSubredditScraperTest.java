package nl.rcomanne.telegrambotklootviool.scraper.reddit;

import nl.rcomanne.telegrambotklootviool.domain.Subreddit;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.scraper.reddit.domain.Child;
import nl.rcomanne.telegrambotklootviool.scraper.reddit.domain.ChildData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class RedditSubredditScraperTest {
    private RestTemplate restTemplate;
    private RedditSubredditScraper sut;

    private Child child;

    @Before
    public void setUp() {
        restTemplate = mock(RestTemplate.class);
        sut = new RedditSubredditScraper(restTemplate);

        child = new Child();
        child.setKind("t3");

        ChildData data = new ChildData();
        data.setId("no-media");
        data.setUrl("no-url");
    }

    @Test
    public void convertItems() {
        List<Child> entries = new ArrayList<>();

        ChildData childData = new ChildData();
        childData.setMedia(null);

        Child child = new Child();
        child.setData(childData);

        entries.add(child);

        Subreddit subreddit = Subreddit.builder()
                .name("test")
                .images(new ArrayList<>())
                .lowestFromAll(200L)
                .threshold(100L)
                .build();

        List<SubredditImage> images = sut.convertItems(subreddit, entries);
        assertFalse(images.get(0).isAnimated());
    }
}
