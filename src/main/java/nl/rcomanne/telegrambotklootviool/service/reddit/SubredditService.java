package nl.rcomanne.telegrambotklootviool.service.reddit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.Subreddit;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.repositories.SubredditImageRepository;
import nl.rcomanne.telegrambotklootviool.repositories.SubredditRepository;
import nl.rcomanne.telegrambotklootviool.scraper.reddit.RedditSubredditScraper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    private final Random r = new Random();

    public SubredditImage findRandom() {
        log.info("finding random image");
        List<SubredditImage> images = imageRepository.findAll();

        if (!images.isEmpty()) {
            Collections.shuffle(images);
            return images.get(r.nextInt(images.size()));
        } else {
            throw new IllegalStateException("no images found");
        }
    }

    public SubredditImage findRandomBySubreddit(final String subredditName) {
        final String cleanSubreddit = subredditName.toLowerCase().trim();
        log.info("finding random image from {}", cleanSubreddit);

        Subreddit subreddit = findOrCreateSubreddit(subredditName);
        List<SubredditImage> images = imageRepository.findAllBySubreddit(subreddit);

        if (images.isEmpty()) {
            images = scrapeAndSaveAllTime(subreddit);
        } else {
            scrapeSubredditAsync(subreddit);
        }

        Collections.shuffle(images);
        return images.get(r.nextInt(images.size()));
    }

    @Async
    public void scrapeSubredditAsync(Subreddit subreddit) {
        log.debug("scraping and saving async for subreddit: '{}'", subreddit.getName());

        if (subreddit.getLastUpdated().isBefore(LocalDateTime.now().minusDays(1))) {
            log.info("subreddit '{}' update has been more than one day ago, updating now...", subreddit);

            final String window = decideWindow(subreddit.getLastUpdated());
            List<SubredditImage> items = scraper.scrapeSubreddit(subreddit, window);

            log.debug("scraped {} items for subreddit {}", items.size(), subreddit);
            cleandAndSave(items, subreddit);
        } else {
            log.info("subreddit '{}' doesn't have to be updated, last update was: {}", subreddit, subreddit.getLastUpdated());
        }
    }

    private Subreddit findSubreddit(String subredditName) {
        return subredditRepository.findById(subredditName).orElseThrow(() -> new IllegalStateException("No subreddit found with name " + subredditName));
    }

    private Subreddit createSubreddit(String subredditName) {
        return subredditRepository.save(Subreddit.builder()
                .name(subredditName)
                .images(new ArrayList<>())
                .build());
    }

    private Subreddit findOrCreateSubreddit(String subredditName) {
        if (!subredditRepository.existsById(subredditName)) {
            return createSubreddit(subredditName);
        }
        return findSubreddit(subredditName);
    }

    private List<SubredditImage> scrapeAndSave(Subreddit subreddit, String window) {
        log.info("scrape and save for {}", subreddit.getName());
        List<SubredditImage> images = scraper.scrapeSubreddit(subreddit, window);
        return cleandAndSave(images, subreddit);
    }

    public List<SubredditImage> scrapeAndSaveAllTime(String subredditName) {
        log.info("scraping and saving {} for all time", subredditName);
        return scrapeAndSaveAllTime(findOrCreateSubreddit(subredditName));
    }

    private List<SubredditImage> scrapeAndSaveAllTime(Subreddit subreddit) {
        log.info("scraping and saving {} for all time", subreddit.getName());
        List<SubredditImage> images = scraper.scrapeSubreddit(subreddit, "all");
        subreddit.setLowestFromAll(getLowestScore(images));
        subreddit.setThreshold(subreddit.getLowestFromAll() / 2);
        log.debug("lowest score for subreddit {} is {}", subreddit.getName(), subreddit.getLowestFromAll());
        return cleandAndSave(images, subreddit);
    }

    private long getLowestScore(List<SubredditImage> images) {
        long lowest = Integer.MAX_VALUE;
        for (SubredditImage image : images) {
            lowest = Math.min(image.getScore(), lowest);
        }
        return lowest;
    }

    private List<SubredditImage> cleandAndSave(List<SubredditImage> images, final Subreddit subreddit) {
        log.info("saving {} items for {}", images.size(), subreddit.getName());
        List<SubredditImage> cleanList = cleanList(images, subreddit);
        imageRepository.saveAll(cleanList);
        updateSubredditLastUpdated(subreddit);
        log.info("saved {} items from subreddit {}", cleanList.size(), subreddit.getName());
        return images;
    }

    public List<SubredditImage> weeklyUpdate(Subreddit subreddit) {
        if (subreddit.getLastUpdated().isBefore(LocalDateTime.now().minusWeeks(1))) {
            log.debug("subreddit '{}' update has been more than one week ago, updating...", subreddit.getName());
            return scrapeAndSave(subreddit, "week");
        } else {
            log.debug("subreddit '{}' has been updated in the past week, no need to update", subreddit.getName());
            return new ArrayList<>();
        }
    }

    private void updateSubredditLastUpdated(Subreddit subreddit) {
        subreddit.setLastUpdated(LocalDateTime.now());
        subredditRepository.save(subreddit);
    }

    public void resetLastUpdatedForSubreddit(String subredditName) {
        Subreddit subreddit = findOrCreateSubreddit(subredditName);
        subreddit.setLastUpdated(LocalDateTime.now().minusYears(1));
        subredditRepository.save(subreddit);
    }

    public void removeSubredditIfEmpty(Subreddit subreddit) {
        if (imageRepository.findFirstBySubreddit(subreddit).isEmpty()) {
            log.debug("Subreddit {} is empty, removing...", subreddit.getName());
            removeSubreddit(subreddit);
        } else {
            log.debug("Subreddit {} is NOT empty, do not remove", subreddit.getName());
        }
    }

    public void removeSubreddit(Subreddit subreddit) {
        imageRepository.deleteAllBySubreddit(subreddit);
        subredditRepository.deleteSubredditByName(subreddit.getName());
    }

    public List<Subreddit> getAllSubreddits() {
        return subredditRepository.findAll();
    }

    public boolean subredditExistsAndHasItems(String subredditName) {
        try {
            Subreddit foundSubreddit = findSubreddit(subredditName);
            imageRepository.findFirstBySubreddit(foundSubreddit).orElseThrow(() -> new IllegalStateException("no images found for subreddit " + subredditName));
        } catch (IllegalStateException ex) {
            log.info("subreddit {} not found, or empty", subredditName);
            log.debug("Exception: {}", ex.getMessage(), ex);
            return false;
        }
        return true;
    }
}
