package nl.rcomanne.telegrambotklootviool.repositories;

import nl.rcomanne.telegrambotklootviool.domain.MemeItem;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemeItemRepository extends MongoRepository<MemeItem, String> {
}
