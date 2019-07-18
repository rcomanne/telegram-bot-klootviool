package nl.rcomanne.telegrambotklootviool.repositories;

import java.util.List;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubredditImageRepository extends MongoRepository<SubredditImage, String> {

    List<SubredditImage> findDistinctBySubreddit(String subreddit);

}
