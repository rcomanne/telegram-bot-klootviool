package nl.rcomanne.telegrambotklootviool.repositories;

import nl.rcomanne.telegrambotklootviool.domain.NsfwItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NsfwItemRepository extends MongoRepository<NsfwItem, String> {
}
