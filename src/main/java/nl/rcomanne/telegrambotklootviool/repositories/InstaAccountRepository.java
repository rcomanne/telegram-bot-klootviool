package nl.rcomanne.telegrambotklootviool.repositories;


import nl.rcomanne.telegrambotklootviool.domain.InstaAccount;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InstaAccountRepository extends MongoRepository<InstaAccount, String> {

    boolean existsByName(String accountName);
}
