package nl.rcomanne.telegrambotklootviool.repositories;

import nl.rcomanne.telegrambotklootviool.domain.PhItem;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhItemRepository extends MongoRepository<PhItem, String> {
}
