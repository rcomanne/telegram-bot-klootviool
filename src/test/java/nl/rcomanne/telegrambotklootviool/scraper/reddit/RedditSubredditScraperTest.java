package nl.rcomanne.telegrambotklootviool.scraper.reddit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.scraper.reddit.domain.Child;
import nl.rcomanne.telegrambotklootviool.scraper.reddit.domain.ChildData;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

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
    }

    @Test
    public void convertItems() {
        List<Child> entries = new ArrayList<>();

        ChildData childData = new ChildData();
        childData.setMedia(null);

        Child child = new Child();
        child.setData(childData);

        entries.add(child);


        List<SubredditImage> images = sut.convertItems("test", entries);
        assertFalse(images.get(0).isAnimated());
    }
}
