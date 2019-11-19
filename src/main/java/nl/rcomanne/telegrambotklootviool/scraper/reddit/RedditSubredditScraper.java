package nl.rcomanne.telegrambotklootviool.scraper.reddit;

import java.util.ArrayList;
import java.util.List;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.scraper.SubredditScraper;
import nl.rcomanne.telegrambotklootviool.scraper.reddit.domain.Child;
import nl.rcomanne.telegrambotklootviool.scraper.reddit.domain.ChildData;
import nl.rcomanne.telegrambotklootviool.scraper.reddit.domain.RedditSubredditResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.annotations.VisibleForTesting;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedditSubredditScraper implements SubredditScraper {
    private static final int DEFAULT_LIMIT = 100;

    @Value("${reddit.url.topAfter}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    @Override
    public List<SubredditImage> scrapeSubreddit(String subreddit, String window) {
        log.debug("start scraping subreddit '{}' from Reddit API", subreddit);
        int request = 0;
        String after = "";
        List<SubredditImage> images = new ArrayList<>();
        do {
            try {
                if (request >= 10) {
                    log.debug("retrieved 10 pages already, we do not need more");
                    return images;
                }
                request++;
                RedditSubredditResponse response = retrieveItems(subreddit, window, after);
                if (response.getData() == null || response.getData().getChildren().isEmpty()) {
                    // got empty page - no more images
                    log.debug("received empty response - stopping and saving");
                    return images;
                }
                final List<Child> items = response.getData().getChildren();
                // adding all retrieved items to the list
                log.debug("received {} entries, adding to images list", items.size());
                images.addAll(convertItems(subreddit, items));
                after = response.getData().getAfter();
            } catch (RuntimeException ex) {
                log.debug("a request failed, return images we have now");
                return images;
            }
        } while (images.size() % 100 == 0);
        return images;
    }

    private RedditSubredditResponse retrieveItems(String subreddit, String window, String after) {
        final String url = baseUrl
            .replace("{subreddit}", subreddit)
            .replace("{window}", window)
            .replace("{limit}", Integer.toString(DEFAULT_LIMIT))
            .replace("{after}", after);

        log.debug("scraping reddit with url {}", url);
        HttpHeaders headers = new HttpHeaders();
        // We need to set the user-agent header, else we get a 429 after 2 pages.
        headers.set("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:70.0) Gecko/20100101 Firefox/70.0");
        HttpEntity entity = new HttpEntity(headers);
        try {
            ResponseEntity<RedditSubredditResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, RedditSubredditResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                if (response.getBody() != null) {
                    return response.getBody();
                } else {
                    log.warn("retrieved body is null");
                }
            } else {
                log.warn("received {} error code... we probably can no longer scrape!", response.getStatusCodeValue());
            }
        } catch (Exception ex) {
            log.warn("received client exception while retrieving images from Reddit. '{}'", ex.getMessage(), ex);
        }
        return new RedditSubredditResponse();
    }

    @VisibleForTesting
    List<SubredditImage> convertItems(String subreddit, List<Child> entries) {
        final List<SubredditImage> convertedItems = new ArrayList<>(entries.size());
        for (Child child : entries) {
            try {
                ChildData data = child.getData();
                log.trace("converting child '{}'", data.getId());
                convertedItems.add(SubredditImage.builder()
                    .id(data.getId())
                    .title(data.getTitle())
                    .imageLink(data.getUrl())
                    .subreddit(subreddit)
                    .animated(child.isAnimated())
                    .nsfw(data.isOver18())
                    .score(data.getScore())
                    .source(child.getSource())
                    .build());
            } catch (Exception e) {
                log.warn("failed to convert item: {}", child, e);
            }
        }
        log.info("converted {} items", convertedItems.size());
        return convertedItems;
    }

}
