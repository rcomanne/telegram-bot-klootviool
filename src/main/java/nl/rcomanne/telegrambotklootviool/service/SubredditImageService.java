package nl.rcomanne.telegrambotklootviool.service;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.repositories.SubredditImageRepository;
import nl.rcomanne.telegrambotklootviool.scraper.DankMemesScraper;
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
    private final DankMemesScraper dankMemesScraper;
    private final SubredditImageRepository repository;
    private final MongoTemplate template;

    private final Random r = new Random();

    private static final int SAMPLE_SIZE = 5;
    private static final String SUBREDDIT_MATCH_KEY = "subreddit";
    private static final String TITLE_MATCH_KEY = "title";
    private static final String DEF_WINDOW = "day";

    private static final String GONEWILD_CLEAN_REGEX = "/(\\(|\\[)(\\s+)?m(ale)?(\\s+)?(\\)|\\])/gi";

    public SubredditImage findRandom() {
        log.info("finding random image");
        SampleOperation sampleStage = Aggregation.sample(SAMPLE_SIZE);
        Aggregation aggregation = Aggregation.newAggregation(sampleStage);

        List<SubredditImage> images = template.aggregate(aggregation, SUBREDDIT_MATCH_KEY, SubredditImage.class).getMappedResults();

        if (!images.isEmpty()) {
            return images.get(r.nextInt(images.size()));
        } else {
            throw new IllegalStateException("no images found");
        }
    }

    public SubredditImage findRandomBySubreddit(final String subreddit) {
        final String cleanSubreddit = subreddit.toLowerCase().trim();
        log.info("finding random image from {}", cleanSubreddit);
        MatchOperation matchStage = Aggregation.match(new Criteria(SUBREDDIT_MATCH_KEY).is(cleanSubreddit));
        SampleOperation sampleStage = Aggregation.sample(SAMPLE_SIZE);
        Aggregation aggregation = Aggregation.newAggregation(matchStage, sampleStage);

        return doFind(aggregation, cleanSubreddit);
    }

    public SubredditImage findBySubredditAndTitle(final String subreddit, final String title) {
        final String cleanSubreddit = subreddit.toLowerCase().trim();
        final String cleanTitle = title.toLowerCase().trim();
        log.debug("finding random image from {} with title {}", cleanSubreddit, cleanTitle);
        MatchOperation subredditMatch = Aggregation.match(new Criteria(SUBREDDIT_MATCH_KEY).is(cleanSubreddit));
        String pattern = "/.*(" + cleanTitle + ").*/gi";
        MatchOperation titleMatch = Aggregation.match(new Criteria(TITLE_MATCH_KEY).regex(pattern));
        SampleOperation sampleStage = Aggregation.sample(SAMPLE_SIZE);
        Aggregation aggregation = Aggregation.newAggregation(subredditMatch, titleMatch, sampleStage);

        return doFind(aggregation, cleanSubreddit);
    }

    @Nullable
    private SubredditImage doFind(Aggregation aggregation, String subreddit) {
        List<SubredditImage> images = template.aggregate(aggregation, SUBREDDIT_MATCH_KEY, SubredditImage.class).getMappedResults();
        if (!images.isEmpty()) {
            log.info("found images {}/{} in {}", images.size(), SAMPLE_SIZE, subreddit);
            return images.get(r.nextInt(images.size()));
        } else {
            log.info("no images found, scraping and saving");
            images = scrapeAndSave(subreddit, DEF_WINDOW);
            if (images.isEmpty()) {
                return null;
            } else {
                return images.get(r.nextInt(images.size()));
            }
        }
    }

    public List<SubredditImage> scrapeAndSave(String subreddit, String window) {
        log.info("scrape and save");
        List<SubredditImage> images = dankMemesScraper.scrapeDankMemes(subreddit);
        images.removeIf(SubredditImage::isMale);
        log.info("saving {} items for {}", images.size(), subreddit);
        images = repository.saveAll(images);
        return images;
    }
}
