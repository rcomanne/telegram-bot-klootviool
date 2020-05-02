package nl.rcomanne.telegrambotklootviool.service.instagram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.postaddict.instagram.scraper.Instagram;
import me.postaddict.instagram.scraper.model.Media;
import me.postaddict.instagram.scraper.model.PageObject;
import nl.rcomanne.telegrambotklootviool.domain.InstaAccount;
import nl.rcomanne.telegrambotklootviool.domain.InstaItem;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstagramScraper {

    private static final int PAGE_SIZE = 30;

    public List<InstaItem> scrapeAccount(InstaAccount account) {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();

        try {
            // create Instagram client
            Instagram instagram = new Instagram(httpClient);

            // retrieve ONE page, NOT the first
            PageObject<Media> pageObject = instagram.getMedias(account.getUsername(), 1);

            if (pageObject == null) {
                // if the retrieved PageObject is null, the library failed to find any media for this account.
                throw new IllegalStateException("PageObject is null! No images found for account: " + account);
            }

            // calculate the total number of pages of the account
            int pageCount = pageObject.getCount() / PAGE_SIZE;

            List<InstaItem> convertedItems = new ArrayList<>();

            // get all media of account
            log.debug("scraping '{}' pages for account '{}', this might take a while", pageCount, account);
            pageObject = instagram.getMedias(account.getUsername(), pageCount);
            log.debug("retrieved PageObject has {} entries, retrieving {} pages", pageObject.getCount(), pageCount);

            // convert retrieved media objects to our domain
            for (Media media : pageObject.getNodes()) {
                log.trace("retrieved media: {}", media.toString());
                if (media.getIsVideo()) {
                    continue;
                }
                convertedItems.add(convert(media, account));
            }

            log.debug("converted {} items", convertedItems.size());
            return convertedItems;
        } catch (IOException ex) {
            log.error("IOException while retrieving data from Instagram. {}", ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    private InstaItem convert(Media media, InstaAccount account) {
        return InstaItem.builder()
            .id(media.getId())
            .link(media.getDisplayUrl())
            .likes(media.getLikeCount())
            .account(account)
            .caption(media.getCaption())
            .isVideo(media.getIsVideo())
            .build();
    }

}
