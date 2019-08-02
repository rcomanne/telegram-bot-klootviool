package nl.rcomanne.telegrambotklootviool.scraper.domain;

import java.util.List;

import lombok.Data;

@Data
class AdConfig {
    private List<String> safeFlags;
    private List<String> highRiskFlags;
    private List<String> unsafeFlags;
    private boolean showsAds;
}
