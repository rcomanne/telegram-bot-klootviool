package nl.rcomanne.telegrambotklootviool.utility;

import static nl.rcomanne.telegrambotklootviool.utility.ImageUtility.cleanList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ImageUtilityTest {

    @Test
    public void cleanListTest() {
        SubredditImage emptyTitle = generateImage("", false);
        SubredditImage nsfwEmptyTitle = generateImage("", true);
        SubredditImage nonNsfw = generateImage("Hello this is a long title message I made", false);
        SubredditImage nsfwTitle = generateImage("Hello this is a long title message I made", true);

        SubredditImage nsfwFemaleBrackets = generateImage("I'm [F]emale", true);
        SubredditImage nsfwFemalePar = generateImage("I'm (F)emale", true);
        SubredditImage nonNsfwFemale = generateImage("I'm [F]emale", false);

        SubredditImage nsfwMaleBrackets = generateImage("I'm [M]ale", true);
        SubredditImage nsfwMalePar = generateImage("I'm (M)ale", true);
        SubredditImage nonNsfwMale = generateImage("I'm [M]ale", false);

        List<SubredditImage> images = new ArrayList<>(List.of(
            emptyTitle, nonNsfw, nsfwTitle, nsfwEmptyTitle,
            nsfwFemaleBrackets, nsfwFemalePar, nonNsfwFemale,
            nsfwMaleBrackets, nsfwMalePar, nonNsfwMale
        ));

        cleanList(images);

        assertTrue(images.contains(emptyTitle));
        assertTrue(images.contains(nsfwEmptyTitle));
        assertTrue(images.contains(nonNsfw));
        assertTrue(images.contains(nsfwTitle));

        assertTrue(images.contains(nsfwFemaleBrackets));
        assertTrue(images.contains(nsfwFemalePar));
        assertTrue(images.contains(nonNsfwFemale));

        assertFalse(images.contains(nsfwMaleBrackets));
        assertFalse(images.contains(nsfwMalePar));
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
