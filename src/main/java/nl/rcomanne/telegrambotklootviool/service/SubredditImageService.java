package nl.rcomanne.telegrambotklootviool.service;

import java.util.List;
import java.util.Random;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.repositories.SubredditImageRepository;
import nl.rcomanne.telegrambotklootviool.scraper.ImgurSubredditScraper;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubredditImageService {
    private final ImgurSubredditScraper scraper;
    private final SubredditImageRepository repository;
    private final MongoTemplate template;

    private final Random r = new Random();

    private static final int SAMPLE_SIZE = 5;
    private static final String SUBREDDIT_MATCH_KEY = "subreddit";
    private static final String TITLE_MATCH_KEY = "title";
    private static final String DEF_WINDOW = "day";

    public SubredditImage findRandom() {
        log.debug("finding random image");
        SampleOperation sampleStage = Aggregation.sample(SAMPLE_SIZE);
        Aggregation aggregation = Aggregation.newAggregation(sampleStage);

        List<SubredditImage> images = template.aggregate(aggregation, SUBREDDIT_MATCH_KEY, SubredditImage.class).getMappedResults();

        if (!images.isEmpty()) {
            return images.get(r.nextInt(images.size()));
        } else {
            throw new IllegalStateException("no images found");
        }
    }

    public SubredditImage findRandomBySubreddit(String subreddit) {
        log.debug("finding random image from {}", subreddit);
        MatchOperation matchStage = Aggregation.match(new Criteria(SUBREDDIT_MATCH_KEY).is(subreddit));
        SampleOperation sampleStage = Aggregation.sample(SAMPLE_SIZE);
        Aggregation aggregation = Aggregation.newAggregation(matchStage, sampleStage);

        return doFind(aggregation, subreddit);
    }

    public SubredditImage findBySubredditAndTitle(String subreddit, String title) {
        log.debug("finding random image from {}", subreddit);
        MatchOperation subredditMatch = Aggregation.match(new Criteria(SUBREDDIT_MATCH_KEY).is(subreddit));
        String pattern = "/.*(" + title + ").*/gi";
        MatchOperation titleMatch = Aggregation.match(new Criteria(TITLE_MATCH_KEY).regex(pattern));
        SampleOperation sampleStage = Aggregation.sample(SAMPLE_SIZE);
        Aggregation aggregation = Aggregation.newAggregation(subredditMatch, titleMatch, sampleStage);

        return doFind(aggregation, subreddit);
    }

    private SubredditImage doFind(Aggregation aggregation, String subreddit) {
        List<SubredditImage> images = template.aggregate(aggregation, SUBREDDIT_MATCH_KEY, SubredditImage.class).getMappedResults();

        if (!images.isEmpty()) {
            return images.get(r.nextInt(images.size()));
        } else {
            images = repository.saveAll(scraper.scrapeSubreddit(subreddit, DEF_WINDOW));
            return images.get(r.nextInt(images.size()));
        }
    }

    public List<SubredditImage> scrapeAndSave(String subreddit, String window) {
        log.debug("scrape and save");
        List<SubredditImage> images = scraper.scrapeSubreddit(subreddit, window);
        return repository.saveAll(images);
    }
}
