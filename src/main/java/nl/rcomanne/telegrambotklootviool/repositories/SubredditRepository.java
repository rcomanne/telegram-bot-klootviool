package nl.rcomanne.telegrambotklootviool.repositories;

import nl.rcomanne.telegrambotklootviool.domain.Subreddit;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubredditRepository extends MongoRepository<Subreddit, String> {

}
