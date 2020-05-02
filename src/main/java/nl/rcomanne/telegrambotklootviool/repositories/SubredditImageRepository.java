package nl.rcomanne.telegrambotklootviool.repositories;

import nl.rcomanne.telegrambotklootviool.domain.Subreddit;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface SubredditImageRepository extends CrudRepository<SubredditImage, String> {

    Optional<SubredditImage> findFirstBySubreddit(Subreddit subreddit);

    @NonNull
    List<SubredditImage> findAll();

    List<SubredditImage> findAllBySubreddit(Subreddit subreddit);

    void deleteAllBySubreddit(Subreddit subreddit);
}
