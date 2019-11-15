package nl.rcomanne.telegrambotklootviool.utility;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import nl.rcomanne.telegrambotklootviool.domain.Subreddit;

import org.junit.Test;

public class SubredditUtilityTest {

    @Test
    public void testDecideWindowMonth() {
        Subreddit subreddit = Subreddit.builder()
            .name("test-reddit")
            .lastUpdated(LocalDateTime.now().minusMonths(1))
            .build();

        String window = SubredditUtility.decideWindow(subreddit.getLastUpdated());
        assertEquals("week", window);
    }

    @Test
    public void testDecideWindowWeek() {
        Subreddit subreddit = Subreddit.builder()
            .name("test-reddit")
            .lastUpdated(LocalDateTime.now().minusWeeks(1))
            .build();

        String window = SubredditUtility.decideWindow(subreddit.getLastUpdated());
        assertEquals("week", window);
    }

    @Test
    public void testDecideWindowDay() {
        Subreddit subreddit = Subreddit.builder()
            .name("test-reddit")
            .lastUpdated(LocalDateTime.now().minusDays(1))
            .build();

        String window = SubredditUtility.decideWindow(subreddit.getLastUpdated());
        assertEquals("day", window);
    }
}
