package nl.rcomanne.telegrambotklootviool.repositories;

import java.util.Optional;

import nl.rcomanne.telegrambotklootviool.domain.Quote;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuoteRepository extends MongoRepository<Quote, String> {
    Optional<Quote> findByMessageContains(String message);
}
