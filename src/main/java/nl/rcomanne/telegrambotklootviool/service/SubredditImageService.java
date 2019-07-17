package nl.rcomanne.telegrambotklootviool.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.repositories.SubredditItemRepository;
import nl.rcomanne.telegrambotklootviool.scraper.ImgurSubredditScraper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubredditImageService {
    private final ImgurSubredditScraper scraper;
    private final SubredditItemRepository repository;
    private final MongoTemplate template;

    private final Random r = new Random();

    private static final int SAMPLE_SIZE = 5;
    private static final String MATCH_KEY = "subreddit";
    private static final String DEF_WINDOW = "day";

    public SubredditImage findRandom() {
        log.debug("finding random image");
        SampleOperation sampleStage = Aggregation.sample(SAMPLE_SIZE);
        Aggregation aggregation = Aggregation.newAggregation(sampleStage);

        List<SubredditImage> images = template.aggregate(aggregation, MATCH_KEY, SubredditImage.class).getMappedResults();

        if (!images.isEmpty()) {
            return images.get(r.nextInt(images.size()));
        } else {
            throw new IllegalStateException("no images found");
        }
    }

    public SubredditImage find(String subreddit) {
        log.debug("finding random image from {}", subreddit);
        MatchOperation matchStage = Aggregation.match(new Criteria(MATCH_KEY).is(subreddit));
        SampleOperation sampleStage = Aggregation.sample(SAMPLE_SIZE);
        Aggregation aggregation = Aggregation.newAggregation(matchStage, sampleStage);

        List<SubredditImage> images = template.aggregate(aggregation, MATCH_KEY, SubredditImage.class).getMappedResults();

        if (!images.isEmpty()) {
            return images.get(r.nextInt(images.size()));
        } else {
            images = repository.saveAll(scraper.scrapeSubreddit(subreddit, DEF_WINDOW));
            return images.get(r.nextInt(images.size()));
        }
    }

    public List<String> scrapeAndSave(String subreddit, String window) {
        log.debug("scrape and save");
        List<SubredditImage> images = scraper.scrapeSubreddit(subreddit, window);
        repository.saveAll(images);
        return images.parallelStream()
                .map(SubredditImage::getId)
                .collect(Collectors.toList());
    }
}
