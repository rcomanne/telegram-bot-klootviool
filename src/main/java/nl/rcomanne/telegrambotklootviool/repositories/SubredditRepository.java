package nl.rcomanne.telegrambotklootviool.repositories;

import nl.rcomanne.telegrambotklootviool.domain.Subreddit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface SubredditRepository extends CrudRepository<Subreddit, String> {

    void deleteSubredditByName(String name);
    @NonNull
    List<Subreddit> findAll();

}
