package nl.rcomanne.telegrambotklootviool.service.instagram;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import nl.rcomanne.telegrambotklootviool.domain.InstaAccount;
import nl.rcomanne.telegrambotklootviool.domain.InstaItem;
import nl.rcomanne.telegrambotklootviool.repositories.InstaAccountRepository;
import nl.rcomanne.telegrambotklootviool.repositories.InstaItemRepository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstagramService {

    private static final String INSTAGRAM_KEY = "instagram";
    private static final String INSTA_ACCOUNT_KEY = "fromUser";
    private static final int SAMPLE_SIZE = 5;

    private final Random r = new Random();

    private final InstagramScraper scraper;

    private final InstaAccountRepository accountRepository;
    private final InstaItemRepository repository;
    private final MongoTemplate template;

    public InstaItem getRandomImage() {
        log.debug("retrieving random instagram image");
        SampleOperation sampleStage = Aggregation.sample(SAMPLE_SIZE);
        Aggregation aggregation = Aggregation.newAggregation(sampleStage);
        return getItem(aggregation);
    }

    public InstaItem getRandomImageFromAccount(String instaAccount) {
        log.debug("retrieving random image from InstaAccount: {}", instaAccount);

        // create match operations for MongoTemplate retrieval, as this run on Rabobank, we do not want any NSFW images
        // match the given Instagram Account
        MatchOperation instaFilter = Aggregation.match(new Criteria(INSTA_ACCOUNT_KEY).is(instaAccount));
        // retrieve a sample of size SAMPLE_SIZE, this is a sort of random retrieval
        SampleOperation sampleStage = Aggregation.sample(SAMPLE_SIZE);
        // combine the operations into an aggregation filter
        Aggregation aggregation = Aggregation.newAggregation(instaFilter, sampleStage);

        try {
            return getItem(aggregation);
        } catch (IllegalArgumentException ex) {
            // no entries found for account, scrape and save from account and then try to return again
            log.warn("nothing found for InstaAccount: {}, start scraping", instaAccount);
            List<InstaItem> items = scraper.scrapeAccount(instaAccount);
            if (!items.isEmpty()) {
                log.debug("retrieved '{}' items from insta account '{}'", items.size(), instaAccount);
                repository.saveAll(items);
                return getItem(aggregation);
            } else {
                log.debug("no items found for insta account '{}'", instaAccount);
                throw new IllegalArgumentException("no items found for insta account " + instaAccount);
            }
        }
    }

    @Async
    public void scrapeInstaAsync(String accountName) {
        InstaAccount account = findOrCreateInstaAccount(accountName);

        if (account.getLastUpdated().isAfter(LocalDateTime.now().minusWeeks(1))) {
            log.debug("Instagram Account '{}' has been updated at '{}', no need to update", account.getName(), account.getLastUpdated());
            return;
        }

        log.debug("scraping insta account '{}' async", accountName);
        List<InstaItem> items = scraper.scrapeAccount(accountName);
        log.debug("scraped and save {} items for insta account {}", items.size(), accountName);
        repository.saveAll(items);
    }

    private InstaAccount findOrCreateInstaAccount(String accountName) {
        return accountRepository.findById(accountName)
            .orElse(accountRepository.save(InstaAccount.builder()
                .name(accountName)
                .lastUpdated(LocalDateTime.now().minusMonths(1))
                .build()));
    }

    private InstaItem getItem(Aggregation aggregation) {
        log.debug("getting item from Instagram account with aggregation");
        List<InstaItem> items = template.aggregate(aggregation, INSTAGRAM_KEY, InstaItem.class)
            .getMappedResults();
        if (items.isEmpty()) {
            // received null
            throw new IllegalArgumentException("no items found!");
        } else {
            return items.get(r.nextInt(items.size()));
        }
    }
}
