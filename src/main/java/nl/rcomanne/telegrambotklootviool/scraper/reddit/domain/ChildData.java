package nl.rcomanne.telegrambotklootviool.scraper.reddit.domain;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChildData {

    private long approvedAtUtc;
    private String subreddit;
    private String selftext;
    private String authorFullname;
    private boolean saved;
    private String modReasonTitle;
    private int gilded;
    private boolean clicked;
    private String title;
    private List<String> linkFlairRichtext;
    private String subredditNamePrefixed;
    private boolean hidden;
    private int pwls;
    private String linkFlairCssClass;
    private int downs;
    private int thumbnailHeight;
    private boolean hideScore;
    private String name;
    private boolean quarantine;
    private String linkFlairTextColor;
    private String authorFlairBackgroundColor;
    private String subredditType;
    private int ups;
    private int totalAwardsReceived;
    private List<String> mediaEmbed;
    private int thumbnailWidth;
    private String authorFlairTemplateId;
    private boolean isOriginalContent;
    private List<Object> userReports;
    private Object secureMedia;
    private boolean isRedditMediaDomain;
    private boolean isMeta;
    private String category;
    private List<String> secureMediaEmbed;
    private String linkFlairText;
    private boolean canModPost;
    private int score;
    private String approvedBy;
    private String thumbnail;
//    private float edited;
    private String authorFlairCssClass;
    private List<String> stewardReports;
    private List<String> authorFlairRichtext;
    private Gildings gildings;
    private String postHint;
    private Object contentCategories;
    private boolean isSelf;
    private String modNote;
    private float created;
    private String linkFlairType;
    private int wls;
    private String bannedBy;
    private String authorFlairType;
    private String domain;
    private boolean allowLiveComments;
    private String selftextHtml;
    private Object likes;
    private Object suggestedSort;
    private Object bannedAtUtc;
    private Object viewCount;
    private boolean archived;
    private boolean noFollow;
    private boolean isCrosspostable;
    private boolean pinned;
    private boolean over18;
    private Preview preview;
    private List<AllAwarding> allAwardings;
    private List<Object> awarders;
    private boolean mediaOnly;
    private boolean canGild;
    private boolean spoiler;
    private boolean locked;
    private Object authorFlairText;
    private boolean visited;
    private Object numReports;
    private Object distinguished;
    private String subredditId;
    private Object modReasonBy;
    private Object removalReason;
    private String linkFlairBackgroundColor;
    private String id;
    private boolean isRobotIndexable;
    private Object reportReasons;
    private String author;
    private Object discussionType;
    private int numComments;
    private boolean sendReplies;
    private String whitelistStatus;
    private boolean contestMode;
    private List<Object> modReports;
    private boolean authorPatreonFlair;
    private Object authorFlairTextColor;
    private String permalink;
    private String parentWhitelistStatus;
    private boolean stickied;
    private String url;
    private int subredditSubscribers;
    private float createdUtc;
    private int numCrossposts;
    private Media media;
    private boolean isVideo;
}
