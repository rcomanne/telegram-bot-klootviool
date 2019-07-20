package nl.rcomanne.telegrambotklootviool.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class GoogleSearchService {
    @Value("${google.key}")
    private String googleKey;

    @Value("${google.cx}")
    private String googleCx;

    private static final String BASE_URL = "https://www.googleapis.com/customsearch/v1";

    private static final String KEY = "?key=%s";
    private static final String CX = "&cx=%s";

    private static final String QUERY_PARAM = "&q=%s";

    private static final String IMG_SEARCH_TYPE = "&searchType=image";
    private static final String IMG_SEARCH_FIELDS = "&fields=items(link)&imgType=photo";

    private static final String SAFE = "&safe=active";

    private static final String IMAGE_SEARCH_TEMPLATE = BASE_URL + KEY + CX + IMG_SEARCH_TYPE + IMG_SEARCH_FIELDS + QUERY_PARAM;

    public String searchImageNSFW(String query) {
        log.info("finding nsfw pictures for query '{}'", query);
        return searchImage(formatUrl(query));
    }

    public String searchImageSFW(String query) {
        log.info("finding sfw pictures for query '{}'", query);
        return searchImage(formatUrl(query) + SAFE);
    }

    private String searchImage(String url) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            log.debug("executing GET: \"{}\"", url);
            HttpGet request = new HttpGet(url);
            return client.execute(request, new ImageResponseHandler());
        } catch (IOException e) {
            log.error("IOException while searching image: {}", e.getMessage(), e);
            return "something went wrong";
        }
    }

    private String formatUrl(String query) {
        String url = String.format(IMAGE_SEARCH_TEMPLATE, googleKey, googleCx, query);
        return url.replaceAll(" ", "+");
    }
}
