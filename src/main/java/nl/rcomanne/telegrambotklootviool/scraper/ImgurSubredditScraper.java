package nl.rcomanne.telegrambotklootviool.scraper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.scraper.domain.ImgurSubredditResponse;
import nl.rcomanne.telegrambotklootviool.scraper.domain.ImgurSubredditResponseItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImgurSubredditScraper {
    @Value("${imgur.url.subreddit}")
    private String baseUrl;

    @Value("${imgur.clientId}")
    private String clientId;

    private final RestTemplate restTemplate;

    public List<SubredditImage> scrapeSubreddit(String subreddit, String window) {
        int page = 0;
        List<SubredditImage> images = new ArrayList<>();
        do {
            try {
                ImgurSubredditResponse subredditResponse = retrieveItems(subreddit, page++, window);
                if (subredditResponse == null) {
                    log.warn("subreddit response is null");
                    throw new IllegalStateException("expected correct response from subreddit");
                }
                if (subredditResponse.getData().isEmpty()) {
                    // got empty page -- no more images
                    return images;
                }
                // adding all retrieved items to the list
                images.addAll(convertItems(subredditResponse));
            } catch (IllegalStateException ex) {
                log.debug("a request failed, return images we have now");
                return images;
            }
        } while (images.size() % 100 == 0);
        return images;
    }

    private List<SubredditImage> convertItems(ImgurSubredditResponse response) {
        List<SubredditImage> images = new ArrayList<>();
        for (ImgurSubredditResponseItem item : response.getData()) {
            log.debug("converting item: {}", item);
            images.add(SubredditImage.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .animated(item.isAnimated())
                    .nsfw(item.isNsfw())
                    .imageLink(item.getLink())
                    .subreddit(item.getSection().toLowerCase())
                    .build());
        }
        return images;
    }

    private ImgurSubredditResponse retrieveItems(String subreddit, int page, String window) {
        final String url = baseUrl + "/" + subreddit + "/top" + "/" + window + "/" + page;
        log.debug("scraping with url {}", url);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, clientId);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<ImgurSubredditResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, ImgurSubredditResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            log.warn("received non 2xx status code {}", response.getStatusCodeValue());
            throw new IllegalStateException("expected correct response from subreddit");
        }
    }
}
