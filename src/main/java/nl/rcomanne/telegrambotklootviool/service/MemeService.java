package nl.rcomanne.telegrambotklootviool.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.MemeItem;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.repositories.MemeItemRepository;
import nl.rcomanne.telegrambotklootviool.repositories.SubredditImageRepository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemeService {
    private static final String KEY = "subreddit";
    private static final String VALUE = "memes";

    private final MemeItemRepository repository;
    private final SubredditImageRepository subredditImageRepository;
    private final MongoTemplate mongoTemplate;

    private final Random r = new Random();

    private static final int SAMPLE_SIZE = 5;

    public List<String> saveFromScraper(List<SubredditImage> images) {
        List<String> ids = new ArrayList<>();
        for (SubredditImage image : images) {
            MemeItem memeItem = MemeItem.builder()
                    .id(image.getId())
                    .title(image.getTitle())
                    .animated(image.isAnimated())
                    .nsfw(image.isNsfw())
                    .imageLink(image.getImageLink())
                    .build();
            ids.add(memeItem.getId());
            repository.save(memeItem);
        }
        subredditImageRepository.saveAll(images);
        return ids;
    }

    public SubredditImage getRandom() {
        MatchOperation matchStage = Aggregation.match(new Criteria(KEY).is(VALUE));
        SampleOperation sampleStage = Aggregation.sample(SAMPLE_SIZE);
        Aggregation aggregation = Aggregation.newAggregation(matchStage, sampleStage);
        List<SubredditImage> items = mongoTemplate.aggregate(aggregation, KEY, SubredditImage.class).getMappedResults();
        if (items.isEmpty()) {
            throw new IllegalStateException("no pictures found :(");
        }
        return items.get(r.nextInt(items.size()));
    }
}
