package nl.rcomanne.telegrambotklootviool.service.reddit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.Subreddit;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.repositories.SubredditImageRepository;
import nl.rcomanne.telegrambotklootviool.repositories.SubredditRepository;
import nl.rcomanne.telegrambotklootviool.scraper.reddit.RedditSubredditScraper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static nl.rcomanne.telegrambotklootviool.utility.ImageUtility.cleanList;
import static nl.rcomanne.telegrambotklootviool.utility.SubredditUtility.decideWindow;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubredditService {
    private final RedditSubredditScraper scraper;
    private final SubredditImageRepository imageRepository;
    private final SubredditRepository subredditRepository;
    private final MongoTemplate template;

    private final Random r = new Random();

    private static final int SAMPLE_SIZE = 5;
    private static final String SUBREDDIT_COLL_NAME = SubredditImage.COLLECTION_NAME;
    private static final String SUBREDDIT_KEY = SubredditImage.SUBREDDIT_KEY;

    public SubredditImage findRandom() {
        log.info("finding random image");
        SampleOperation sampleStage = Aggregation.sample(SAMPLE_SIZE);
        Aggregation aggregation = Aggregation.newAggregation(sampleStage);

        List<SubredditImage> images = template.aggregate(aggregation, SUBREDDIT_COLL_NAME, SubredditImage.class).getMappedResults();

        if (!images.isEmpty()) {
            return images.get(r.nextInt(images.size()));
        } else {
            throw new IllegalStateException("no images found");
        }
    }

    @Nullable
    public SubredditImage findRandomBySubreddit(final String subreddit) {
        final String cleanSubreddit = subreddit.toLowerCase().trim();
        log.info("finding random image from {}", cleanSubreddit);
        MatchOperation matchStage = Aggregation.match(new Criteria(SUBREDDIT_KEY).is(cleanSubreddit));
        SampleOperation sampleStage = Aggregation.sample(SAMPLE_SIZE);
        Aggregation aggregation = Aggregation.newAggregation(matchStage, sampleStage);

        return doFind(aggregation, cleanSubreddit);
    }

    @Nullable
    private SubredditImage doFind(Aggregation aggregation, String subreddit) {
        List<SubredditImage> images = template.aggregate(aggregation, SUBREDDIT_COLL_NAME, SubredditImage.class).getMappedResults();
        if (!images.isEmpty()) {
            log.info("found images {}/{} in {}", images.size(), SAMPLE_SIZE, subreddit);
            return images.get(r.nextInt(images.size()));
        } else {
            log.warn("no images found in {}", subreddit);
            return null;
        }
    }

    @Async
    public void scrapeSubredditAsync(String subredditName) {
        log.debug("scraping and saving async for subreddit: '{}'", subredditName);

        // check repo if it exists, else create entry for it
        Subreddit subreddit = findOrCreateSubreddit(subredditName);

        if (subreddit.getLastUpdated().isBefore(LocalDateTime.now().minusDays(1))) {
            log.info("subreddit '{}' update has been more than one week ago, updating now...", subredditName);

            final String window = decideWindow(subreddit.getLastUpdated());
            List<SubredditImage> items = scraper.scrapeSubreddit(subredditName, window);
            log.debug("scraped {} items for subreddit {}", items.size(), subredditName);
            items = imageRepository.saveAll(items);
            log.debug("saved {} items for subreddit {}", items.size(), subredditName);
            if (!items.isEmpty()) {
                subreddit.setLastUpdated(LocalDateTime.now());
                subredditRepository.save(subreddit);
            }
        } else {
            log.info("subreddit '{}' doesn't have to be updated, last update was: {}", subredditName, subreddit.getLastUpdated());
        }
    }

    private Subreddit findOrCreateSubreddit(String subredditName) {
        if (!subredditRepository.existsById(subredditName)) {
            return subredditRepository.save(Subreddit.builder()
                .name(subredditName)
                .lastUpdated(LocalDateTime.now().minusMonths(1))
                .build());
        } else {
            return subredditRepository.findById(subredditName).orElseThrow();
        }
    }

    private List<SubredditImage> scrapeAndSave(String subredditName, String window) {
        final String cleanSubreddit = subredditName.toLowerCase().trim();
        log.info("scrape and save for {}", cleanSubreddit);
        List<SubredditImage> images = scraper.scrapeSubreddit(cleanSubreddit, window);
        return cleandAndSave(images, subredditName);
    }

    public List<SubredditImage> scrapeAndSaveAllTime(String subredditName) {
        final String cleanSubreddit = subredditName.toLowerCase().trim();
        log.info("scraping and saving {} for all time", cleanSubreddit);
        List<SubredditImage> images = scraper.scrapeSubreddit(cleanSubreddit, "all");
        return cleandAndSave(images, subredditName);
    }

    private List<SubredditImage> cleandAndSave(List<SubredditImage> images, final String subredditName) {
        log.info("saving {} items for {}", images.size(), subredditName);
        List<SubredditImage> cleanList = cleanList(images);
        images = imageRepository.saveAll(cleanList);
        updateSubredditLastUpdated(subredditName);
        log.info("saved {} items from subreddit {}", cleanList.size(), subredditName);
        return images;
    }


    public List<SubredditImage> weeklyUpdate(Subreddit subreddit) {
        if (subreddit.getLastUpdated().isBefore(LocalDateTime.now().minusWeeks(1))) {
            log.debug("subreddit '{}' update has been more than one week ago, updating...", subreddit.getName());
            return scrapeAndSave(subreddit.getName(), "week");
        } else {
            log.debug("subreddit '{}' has been updated in the past week, no need to update", subreddit.getName());
            return new ArrayList<>();
        }
    }

    private void updateSubredditLastUpdated(String subredditName) {
        Subreddit subreddit = findOrCreateSubreddit(subredditName);
        subreddit.setLastUpdated(LocalDateTime.now());
        subredditRepository.save(subreddit);
    }

    public void resetLastUpdatedForSubreddit(String subredditName) {
        Subreddit subreddit = findOrCreateSubreddit(subredditName);
        subreddit.setLastUpdated(LocalDateTime.now().minusYears(1));
        subredditRepository.save(subreddit);
    }

    public void removeSubredditIfEmpty(Subreddit subreddit) {
        if (imageRepository.findFirstBySubreddit(subreddit.getName()).isEmpty()) {
            log.debug("Subreddit {} is empty, removing...", subreddit.getName());
            removeSubreddit(subreddit);
        } else {
            log.debug("Subreddit {} is NOT empty, do not remove", subreddit.getName());
        }
    }

    public void removeSubreddit(Subreddit subreddit) {
        subredditRepository.deleteSubredditByName(subreddit.getName());
        imageRepository.deleteAllBySubreddit(subreddit.getName());
    }

    public List<Subreddit> getAllSubreddits() {
        return subredditRepository.findAll();
    }
}
