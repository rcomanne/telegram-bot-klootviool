package nl.rcomanne.telegrambotklootviool.scraper.reddit.domain;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AllAwarding {

    private int count;

    private boolean isEnabled;

    private String subredditId;

    private String description;

    private long endDate;

    private int coinReward;

    private String iconUrl;

    private int daysOfPremium;

    private String id;

    private int iconHeight;

    private List<ResizedIcon> resizedIcons = null;

    private int daysOfDripExtension;

    private String awardType;

    private long startDate;

    private int coinPrice;

    private int iconWidth;

    private int subredditCoinReward;

    private String name;
}
