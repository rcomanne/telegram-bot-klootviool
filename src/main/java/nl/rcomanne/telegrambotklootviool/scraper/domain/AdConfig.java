package nl.rcomanne.telegrambotklootviool.scraper.domain;

import lombok.Data;

import java.util.List;

@Data
class AdConfig {
    private List<String> safeFlags;
    private List<String> highRiskFlags;
    private List<String> unsafeFlags;
    private boolean showsAds;
}
