package nl.rcomanne.telegrambotklootviool.scraper.domain;

import lombok.Data;

import java.util.List;

@Data
public class AdConfig {
    public List<String> safeFlags = null;
    public List<String> highRiskFlags = null;
    public List<String> unsafeFlags = null;
    public Boolean showsAds;
}
