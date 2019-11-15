package nl.rcomanne.telegrambotklootviool.utility;

import java.time.LocalDateTime;

public class SubredditUtility {
    private SubredditUtility() {}

    public static String decideWindow(LocalDateTime lastUpdated) {
        if (lastUpdated.isBefore(LocalDateTime.now().minusMonths(1))) {
            return "all";
        }
        if (lastUpdated.isBefore(LocalDateTime.now().minusWeeks(1))) {
            return "week";
        } else {
            return "day";
        }
    }

}
