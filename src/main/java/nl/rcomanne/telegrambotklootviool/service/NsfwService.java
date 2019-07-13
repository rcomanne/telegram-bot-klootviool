package nl.rcomanne.telegrambotklootviool.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.NsfwItem;
import nl.rcomanne.telegrambotklootviool.repositories.NsfwItemRepository;
import nl.rcomanne.telegrambotklootviool.web.dto.NsfwDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class NsfwService {
    private final NsfwItemRepository repository;
    private final Random r = new Random();

    public NsfwItem getRandomNsfwItem() {
        log.debug("finding random quote");
        List<NsfwItem> items = repository.findAll();
        return items.get(r.nextInt(items.size()));
    }

    public String save(NsfwDto dto) {
        log.debug("saving ph dto");
        return repository.save(dto.toNsfwItem()).getId();
    }

    public List<String> save(List<NsfwDto> dtos) {
        log.debug("saving list of ph dtos");
        List<String> ids = new ArrayList<>();
        for (NsfwDto dto : dtos) {
            ids.add(save(dto));
        }
        return ids;
    }
}
