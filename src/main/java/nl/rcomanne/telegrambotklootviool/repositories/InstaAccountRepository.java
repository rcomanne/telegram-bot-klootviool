package nl.rcomanne.telegrambotklootviool.repositories;

import nl.rcomanne.telegrambotklootviool.domain.InstaAccount;
import org.springframework.data.repository.CrudRepository;

public interface InstaAccountRepository extends CrudRepository<InstaAccount, String> {
    boolean existsByUsername(String username);
}
