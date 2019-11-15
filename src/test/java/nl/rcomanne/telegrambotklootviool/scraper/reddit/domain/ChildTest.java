package nl.rcomanne.telegrambotklootviool.scraper.reddit.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChildTest {
    private Child child;

    @Before
    public void setUp() {
        child = new Child();
        child.setKind("t3");
    }

    @After
    public void tearDown() {
        child = null;
    }

    @Test
    public void isAnimatedTrue() {
        child = new Child();

        Oembed oembed = new Oembed();
        oembed.setType("video");

        Media media = new Media();
        media.setOembed(oembed);

        ChildData childData = new ChildData();
        childData.setMedia(media);
        childData.setUrl("animated-video");

        child.setData(childData);

        assertTrue(child.isAnimated());
    }

    @Test
    public void isAnimatedFromImgur() {
        ChildData childData = new ChildData();
        childData.setUrl("https://i.imgur.com/KZNJjEx.gifv");

        child.setData(childData);
        assertTrue(child.isAnimated());
    }

    @Test
    public void isAnimatedEmptyString() {
        ChildData childData = new ChildData();
        childData.setUrl("");

        child.setData(childData);
        assertFalse(child.isAnimated());
    }

    @Test
    public void isAnimatedNoUrl() {
        ChildData childData = new ChildData();
        childData.setUrl(null);

        child.setData(childData);
        assertFalse(child.isAnimated());
    }

    @Test
    public void isAnimatedNull() {
        assertFalse(child.isAnimated());
    }

    @Test
    public void isAnimatedMediaNull() {
        ChildData childData = new ChildData();
        childData.setUrl("media-null");
        child.setData(childData);
        assertFalse(child.isAnimated());
    }

    @Test
    public void isAnimatedOemediaNull() {
        ChildData childData = new ChildData();
        Media media = new Media();
        childData.setMedia(media);
        assertFalse(child.isAnimated());
    }
}
