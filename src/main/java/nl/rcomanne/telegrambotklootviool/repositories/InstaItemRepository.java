package nl.rcomanne.telegrambotklootviool.repositories;

import nl.rcomanne.telegrambotklootviool.domain.InstaItem;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InstaItemRepository extends MongoRepository<InstaItem, String> {

}
