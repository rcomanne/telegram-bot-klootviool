package nl.rcomanne.telegrambotklootviool.utility;

import nl.rcomanne.telegrambotklootviool.domain.Subreddit;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static nl.rcomanne.telegrambotklootviool.utility.ImageUtility.cleanList;
import static nl.rcomanne.telegrambotklootviool.utility.ImageUtility.cleanListStream;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ImageUtilityTest {

    @Test
    public void cleanListTest() {
        SubredditImage emptyTitle = generateImage("", false);
        SubredditImage nsfwEmptyTitle = generateImage("", true);
        SubredditImage nonNsfw = generateImage("Hello this is a long title message I made", false);
        SubredditImage nsfwTitle = generateImage("Hello this is a long title message I made", true);

        SubredditImage nsfwFemaleBrackets = generateImage("I'm [F]emale", true);
        SubredditImage nsfwFemaleBracketsL = generateImage("I'm [f]emale", true);
        SubredditImage nsfwFemalePar = generateImage("I'm (F)emale", true);
        SubredditImage nsfwFemaleParL = generateImage("I'm (f)emale", true);

        SubredditImage nsfwMaleBrackets = generateImage("I'm [M]ale", true);
        SubredditImage nsfwMaleBracketsL = generateImage("I'm [m]ale", true);
        SubredditImage nsfwMalePar = generateImage("I'm (M)ale", true);
        SubredditImage nsfwMaleParL = generateImage("I'm (m)ale", true);

        SubredditImage nonNsfwMale = generateImage("I'm [M]ale", false);
        SubredditImage nonNsfwFemale = generateImage("I'm [F]emale", false);

        List<SubredditImage> images = new ArrayList<>(List.of(
            emptyTitle, nonNsfw, nsfwTitle, nsfwEmptyTitle,
            nsfwFemaleBrackets, nsfwFemaleBracketsL, nsfwFemalePar, nsfwFemaleParL, nonNsfwFemale,
            nsfwMaleBrackets, nsfwMaleBracketsL, nsfwMalePar, nsfwMaleParL, nonNsfwMale
        ));

        Subreddit subreddit = Subreddit.builder()
                .name("test")
                .images(new ArrayList<>())
                .lowestFromAll(100L)
                .build();

        List<SubredditImage> cleanList = cleanList(images, subreddit);

        assertTrue(cleanList.contains(emptyTitle));
        assertTrue(cleanList.contains(nsfwEmptyTitle));
        assertTrue(cleanList.contains(nonNsfw));
        assertTrue(cleanList.contains(nsfwTitle));

        assertTrue(cleanList.contains(nsfwFemaleBrackets));
        assertTrue(cleanList.contains(nsfwFemaleBracketsL));
        assertTrue(cleanList.contains(nsfwFemalePar));
        assertTrue(cleanList.contains(nsfwFemaleParL));
        assertTrue(cleanList.contains(nonNsfwFemale));

        assertFalse(cleanList.contains(nsfwMaleBrackets));
        assertFalse(cleanList.contains(nsfwMaleBracketsL));
        assertFalse(cleanList.contains(nsfwMalePar));
        assertFalse(cleanList.contains(nsfwMaleParL));

        assertTrue(cleanList.contains(nonNsfwMale));
    }

    @Test
    public void cleanListStreamTest() {
        SubredditImage emptyTitle = generateImage("", false);
        SubredditImage nsfwEmptyTitle = generateImage("", true);

        SubredditImage nonNsfw = generateImage("Hello this is a long title message I made", false);
        SubredditImage nsfwTitle = generateImage("Hello this is a long title message I made", true);

        SubredditImage nsfwFemaleBrackets = generateImage("I'm [F]emale", true);
        SubredditImage nsfwFemaleBracketsL = generateImage("I'm [f]emale", true);
        SubredditImage nsfwFemalePar = generateImage("I'm (F)emale", true);
        SubredditImage nsfwFemaleParL = generateImage("I'm (f)emale", true);

        SubredditImage nsfwMaleBrackets = generateImage("I'm [M]ale", true);
        SubredditImage nsfwMaleBracketsL = generateImage("I'm [m]ale", true);
        SubredditImage nsfwMalePar = generateImage("I'm (M)ale", true);
        SubredditImage nsfwMaleParL = generateImage("I'm (m)ale", true);

        SubredditImage nonNsfwMale = generateImage("I'm [M]ale", false);
        SubredditImage nonNsfwFemale = generateImage("I'm [F]emale", false);

        List<SubredditImage> images = new ArrayList<>(List.of(
            emptyTitle, nonNsfw, nsfwTitle, nsfwEmptyTitle,
            nsfwFemaleBrackets, nsfwFemaleBracketsL, nsfwFemalePar, nsfwFemaleParL, nonNsfwFemale,
            nsfwMaleBrackets, nsfwMaleBracketsL, nsfwMalePar, nsfwMaleParL, nonNsfwMale
        ));

        cleanListStream(images);

        assertTrue(images.contains(emptyTitle));
        assertTrue(images.contains(nsfwEmptyTitle));
        assertTrue(images.contains(nonNsfw));
        assertTrue(images.contains(nsfwTitle));

        assertTrue(images.contains(nsfwFemaleBrackets));
        assertTrue(images.contains(nsfwFemaleBracketsL));
        assertTrue(images.contains(nsfwFemalePar));
        assertTrue(images.contains(nsfwFemaleParL));
        assertTrue(images.contains(nonNsfwFemale));

        assertFalse(images.contains(nsfwMaleBrackets));
        assertFalse(images.contains(nsfwMaleBracketsL));
        assertFalse(images.contains(nsfwMalePar));
        assertFalse(images.contains(nsfwMaleParL));

        assertTrue(images.contains(nonNsfwMale));
    }

    private SubredditImage generateImage(String title, boolean nsfw) {
        return SubredditImage.builder()
            .id("id")
            .title(title)
            .nsfw(nsfw)
            .build();
    }
}
