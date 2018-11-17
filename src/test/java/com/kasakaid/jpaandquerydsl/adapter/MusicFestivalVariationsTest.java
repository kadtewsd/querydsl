package com.kasakaid.jpaandquerydsl.adapter;

import com.kasakaid.jpaandquerydsl.AbstractBaseTest;
import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class MusicFestivalVariationsTest extends AbstractBaseTest {

    @Autowired
    private MusicFestivalVariations mfVariation;

    @SneakyThrows
    @Before
    public void setUp() {
        super.setup();
        myResource.insertData("music_festival");
    }

    /**
     * 最も細かい粒度がアーティストになります。
     */
    @Test
    public void findMusicFestival() {
        List<MusicFestival> list1 = mfVariation.findMusicFestival();
        assertThat(list1.size(), is(7));
        assertThat(list1.get(0).getArtists().size(), is(0));
    }

    /**
     * 最も細かい粒度がメンバーになりますが、エンティティとして組み立てています。
     * ただし、実装はとてもわかりずらいです。
     */
    @Test
    public void findMusicFestivalByUglyMethod() {
        List<MusicFestival> list1 = mfVariation.findUglyMusicFestival();
        assertThat(list1.size(), is(2));
        assertThat(list1.get(0).getArtists(), notNullValue());
        assertThat(list1.get(0).getArtists().size(), is(1));
        assertThat(list1.get(0).getArtists().get(0).getArtistName(), is("Base Ball Bear"));
        assertThat(list1.get(0).getArtists().get(0).getMembers().size(), is(2));

        assertThat(list1.get(1).getArtists(), notNullValue());
        assertThat(list1.get(1).getArtists().size(), is(1));
        assertThat(list1.get(1).getArtists().get(0).getArtistName(), is("ゲスの極み乙女"));
        assertThat(list1.get(1).getArtists().get(0).getMembers().size(), is(3));
    }

    @Test
    public void subQueryWithMax() {
        List<MusicFestival> musicFestivals = mfVariation.subQueryWithMax();
        assertThat(musicFestivals.size(), is(3));
    }
}