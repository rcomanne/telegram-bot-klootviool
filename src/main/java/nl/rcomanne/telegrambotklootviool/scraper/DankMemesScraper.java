package nl.rcomanne.telegrambotklootviool.scraper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.scraper.domain.DankMemesReponseItem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DankMemesScraper {
    @Value("${dankmemes.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public List<SubredditImage> scrapeDankMemes(String subreddit) {
        log.info("scraping subreddit {} from dankmemes API", subreddit);

        try {
            List<DankMemesReponseItem> reponse = retrieveItems(subreddit);
            if (reponse != null && !reponse.isEmpty()) {

                return reponse.parallelStream()
                    .map(item -> item.toSubredditImage(subreddit))
                    .collect(Collectors.toList());
            }
        } catch (Exception ex) {
            log.error("exception while retrieving items from {}: {}", baseUrl, ex.getMessage(), ex);
        }

        return new ArrayList<>();
    }

    private List<DankMemesReponseItem> retrieveItems(String subreddit) {
        final String url = baseUrl.replace("{subreddit}", subreddit);

        log.debug("retrieving items from {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);

        try {
            ResponseEntity<List<DankMemesReponseItem>> response = restTemplate.exchange(
                // IntelliJ tells me I can removed the explicit type argument, but when I do I get compiler errors
                url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<DankMemesReponseItem>>() {});
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                log.warn("received non 2xx status code {}", response.getStatusCodeValue());
                throw new IllegalStateException("expected 2xx status code from DankMemes API");
            }
        } catch (Exception ex) {
            log.warn("exception while retrieving items: {}", ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

}
