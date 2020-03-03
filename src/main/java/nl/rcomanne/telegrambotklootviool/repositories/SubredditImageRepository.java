package nl.rcomanne.telegrambotklootviool.repositories;

import java.util.Optional;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubredditImageRepository extends MongoRepository<SubredditImage, String> {

    Optional<SubredditImage> findFirstBySubreddit(String subreddit);

    void deleteAllBySubreddit(String subreddit);
}
