package nl.rcomanne.telegrambotklootviool.utility;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;

public class ImageUtility {
    private ImageUtility() {}

    private static final String GENDER_REGEX = "(?:\\(|\\[|\\{).*?([Ff]{1}|[mM]{1}|[tT]{1}).*?(?:\\)|\\]|\\})";
    private static final Pattern GENDER_PATTERN = Pattern.compile(GENDER_REGEX);

    public static void cleanList(List<SubredditImage> images) {
        images.removeIf(image -> {
            if (image.isNsfw()) {
                Matcher matcher = GENDER_PATTERN.matcher(image.getTitle());
                while (matcher.find()) {
                    String match = matcher.group();
                    String gender = match.substring(1, 2);
                    if (gender.equalsIgnoreCase("M")) {
                        return true;
                    }
                }
            }
            return false;
        });
    }

}
