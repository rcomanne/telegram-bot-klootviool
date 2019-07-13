package nl.rcomanne.telegrambotklootviool.repositories;

import nl.rcomanne.telegrambotklootviool.domain.Quote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuoteRepository extends MongoRepository<Quote, String> {
    Optional<Quote> findByMessageContains(String message);
}
