package nl.rcomanne.telegrambotklootviool.repositories;

import nl.rcomanne.telegrambotklootviool.domain.InstaAccount;
import nl.rcomanne.telegrambotklootviool.domain.InstaItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InstaItemRepository extends CrudRepository<InstaItem, String> {

    List<InstaItem> findAll();
    List<InstaItem> findAllByAccount(InstaAccount account);
}
