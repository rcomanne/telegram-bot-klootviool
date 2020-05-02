package nl.rcomanne.telegrambotklootviool.service.instagram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.InstaAccount;
import nl.rcomanne.telegrambotklootviool.domain.InstaItem;
import nl.rcomanne.telegrambotklootviool.repositories.InstaAccountRepository;
import nl.rcomanne.telegrambotklootviool.repositories.InstaItemRepository;
import nl.rcomanne.telegrambotklootviool.service.MessageService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstagramService {

    private final Random r = new Random();

    private final InstagramScraper scraper;

    private final InstaAccountRepository accountRepository;
    private final InstaItemRepository itemRepository;

    private final MessageService messageService;

    public InstaItem getRandomImage() {
        log.info("retrieving random instagram image");
        List<InstaItem> allItems = itemRepository.findAll();
        Collections.shuffle(allItems);
        int index = r.nextInt(allItems.size());
        return allItems.get(index);
    }

    public InstaItem getRandomImageFromAccount(String accountName, long chatId) {
        log.info("retrieving random image from InstaAccount: {}", accountName);

        InstaAccount account;
        if (accountRepository.existsByUsername(accountName)) {
            log.debug("Instagram account is known");
            account = findInstaAccount(accountName);
        } else {
            log.debug("Instagram account is unknown");
            account = createInstaAccount(accountName);
        }

        List<InstaItem> foundItems = itemRepository.findAllByAccount(account);
        if (foundItems.isEmpty()) {
            log.debug("No items found for account {}, scraping", account.getUsername());
            messageService.sendMessage(String.valueOf(chatId), String.format("No items found for %s, scraping now, this could take some time", account.getUsername()));
            List<InstaItem> newItems = scraper.scrapeAccount(account);
            if (newItems.isEmpty()) {
                throw new IllegalArgumentException("No items found for Instagram " + account.getUsername());
            } else {
                itemRepository.saveAll(newItems);
                account.setLastUpdated(LocalDateTime.now());
                accountRepository.save(account);
            }
        } else {
            scrapeInstaAsync(account);
        }
        log.debug("Found entries for account {}", accountName);
        Collections.shuffle(foundItems);
        int index = r.nextInt(foundItems.size());
        return foundItems.get(index);
    }

    @Async
    public void scrapeInstaAsync(InstaAccount account) {
        if (account.getLastUpdated().isAfter(LocalDateTime.now().minusWeeks(1))) {
            log.debug("Instagram Account '{}' has been updated at '{}', no need to update", account.getUsername(), account.getLastUpdated());
            return;
        }

        log.debug("scraping insta account '{}' async", account);
        List<InstaItem> items = scraper.scrapeAccount(account);
        log.debug("scraped and save {} items for insta account {}", items.size(), account);
        itemRepository.saveAll(items);
        account.setLastUpdated(LocalDateTime.now());
        accountRepository.save(account);
    }

    private InstaAccount findInstaAccount(String accountName) {
        return accountRepository.findById(accountName).orElseThrow(() -> new IllegalStateException("InstaAccount not found"));
    }

    private InstaAccount createInstaAccount(String accountName) {
        if (accountRepository.existsByUsername(accountName)) {
            throw new IllegalStateException("Account with that username already exists");
        } else {
            return accountRepository.save(
                    InstaAccount.builder()
                            .username(accountName)
                            .items(new ArrayList<>())
                            .build()
            );
        }
    }
}
