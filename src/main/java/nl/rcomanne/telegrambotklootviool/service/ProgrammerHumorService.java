package nl.rcomanne.telegrambotklootviool.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.PhItem;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.repositories.PhItemRepository;
import nl.rcomanne.telegrambotklootviool.web.dto.PhDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgrammerHumorService {

    private final PhItemRepository repository;
    private final Random r = new Random();

    public PhItem getRandomPhItem() {
        log.debug("finding randomm PH item");
        List<PhItem> items = repository.findAll();
        return items.get(r.nextInt(items.size()));
    }

    public String save(PhDto dto) {
        log.debug("saving ph dto");
        return repository.save(dto.toPhItem()).getId();
    }

    public List<String> save(List<PhDto> dtos) {
        log.debug("saving list of ph dtos");
        List<String> ids = new ArrayList<>();
        for (PhDto dto : dtos) {
            ids.add(save(dto));
        }
        return ids;
    }

    public List<String> saveFromScraper(List<SubredditImage> images) {
        log.debug("saving images to PH images from scraper");
        List<String> ids = new ArrayList<>();
        for (SubredditImage image : images) {
            PhItem item = PhItem.builder()
                    .id(image.getId())
                    .title(image.getTitle())
                    .animated(image.isAnimated())
                    .nsfw(image.isNsfw())
                    .imageLink(image.getImageLink())
                    .build();
            item = repository.save(item);
            ids.add(item.getId());
        }
        return ids;
    }
}
