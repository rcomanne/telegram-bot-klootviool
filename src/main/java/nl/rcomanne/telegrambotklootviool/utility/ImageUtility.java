package nl.rcomanne.telegrambotklootviool.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageUtility {

    private static final String GENDER_REGEX = "(?:\\(|\\[|\\{).*?([Ff]{1}|[mM]{1}|[tT]{1}).*?(?:\\)|\\]|\\})";

    private static final Pattern GENDER_PATTERN = Pattern.compile(GENDER_REGEX, Pattern.CASE_INSENSITIVE);

    @SuppressWarnings("squid:S3776")
    static void cleanListStream(List<SubredditImage> images) {
        images.removeIf(image -> {
            if (image.isNsfw()) {
                Matcher matcher = GENDER_PATTERN.matcher(image.getTitle());
                if (matcher.find()) {
                    String match = matcher.group(1);
                    if (isMale(match, image.getTitle())) {
                        return true;
                    }
                    while (matcher.find()) {
                        match = matcher.group(1);
                        if (isMale(match, image.getTitle())) {
                            return true;
                        }
                    }
                } else {
                    log.debug("no match found for '{}', adding", image.getTitle());
                }
            }
            return false;
        });
    }

    @SuppressWarnings("squid:S3776")
    public static List<SubredditImage> cleanList(List<SubredditImage> images) {
        int sfwCounter = 0;
        int nsfwCounter = 0;
        int maleCounter = 0;
        int femaleCounter = 0;
        int noMatchCounter = 0;

        List<SubredditImage> cleanList = new ArrayList<>();

        for (SubredditImage image : images) {
            if (image.isNsfw()) {
                nsfwCounter++;
                Matcher matcher = GENDER_PATTERN.matcher(image.getTitle());

                if (matcher.find()) {
                    String match = matcher.group(1);
                    if (isMale(match, image.getTitle())) {
                        maleCounter++;
                    } else {
                        femaleCounter++;
                        cleanList.add(image);
                    }
                    while (matcher.find()) {
                        match = matcher.group(1);
                        if (isMale(match, image.getTitle())) {
                            maleCounter++;
                        } else {
                            femaleCounter++;
                        }
                    }
                } else {
                    log.debug("no match found for '{}', adding", image.getTitle());
                    noMatchCounter++;
                    cleanList.add(image);
                }
            } else {
                cleanList.add(image);
                sfwCounter++;
            }
        }
        log.info("cleaned {} SFW items and {} NSFW items, of those {} were male, {} were female, {} of those were no match", sfwCounter, nsfwCounter, maleCounter, femaleCounter, noMatchCounter);
        return cleanList;
    }

    private static boolean isMale(String match, String title) {
        if (match.contains("m") || match.contains("M")) {
            log.debug("found '{}', removing '{}'", match, title);
            return true;
        } else {
            log.debug("found '{}', adding '{}'", match, title);
            return false;
        }
    }

    private ImageUtility() {
    }

}
