package nl.rcomanne.telegrambotklootviool.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.MemeItem;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.repositories.MemeItemRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemeService {
    private final MemeItemRepository repository;
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
        return ids;
    }

    public MemeItem getRandom() {
        SampleOperation sampleStage = Aggregation.sample(SAMPLE_SIZE);
        Aggregation aggregation = Aggregation.newAggregation(sampleStage);
        List<MemeItem> output = mongoTemplate.aggregate(aggregation, "memes", MemeItem.class).getMappedResults();
        if (output.size() == 0) {
            throw new IllegalStateException("no pictures found :(");
        }
        return output.get(r.nextInt(output.size()));
    }
}
