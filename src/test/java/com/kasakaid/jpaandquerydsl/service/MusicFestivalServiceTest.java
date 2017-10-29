package com.kasakaid.jpaandquerydsl.service;

import com.kasakaid.jpaandquerydsl.AbstractBaseTest;
import com.kasakaid.jpaandquerydsl.Application;
import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.domain.artist.MemberInformation;
import com.kasakaid.jpaandquerydsl.repository.NormalJpaRepository;
import com.kasakaid.jpaandquerydsl.spring.TestConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

@SpringBootTest(classes = {Application.class, TestConfig.class})
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Slf4j
public class MusicFestivalServiceTest extends AbstractBaseTest {

    @SneakyThrows
    @Before
    public void setUp() {
        super.setup();
        myResource.insertData("music_festival");
    }

    @Autowired
    private MusicFestivalService service;

    @Test
    public void selectJPAQueryFactory() {
        List<MusicFestival> list = service.findAll();
        assertThat(list.size(), is(3));
        List<MusicFestival> list0 = service.findMusicFestivalByJPAQuery();
        assertThat(list0.get(0).getArtists(), notNullValue());
        assertThat(list0.get(0).getArtists().size(), greaterThan(0));
        assertThat(list0.size(), is(1));
    }

    @Test
    public void leftJoinTableTest() {
        List<MusicFestival> list = service.findAll();
        assertThat(list.size(), is(3));
        List<MusicFestival> list0 = service.findMusicFestival();
        assertThat(list0.get(0).getArtists(), notNullValue());
        assertThat(list0.get(0).getArtists().size(), greaterThan(0));
        assertThat(list0.size(), is(3));
        list.forEach(x -> log.info(x.getFestivalName()));
    }

    @Test
    public void whereJoinTableTest() {
        List<MusicFestival> list1 = service.findMusicFestivalWhereJoin();
        assertThat(list1.get(0).getArtists(), notNullValue());
        assertThat(list1.size(), is(1));
        assertThat(list1.get(0).getArtists().size(), greaterThan(0));
    }
    @Test
    public void findByTransFormTest() {
        List<MusicFestival> list1 = service.findMusicFestivalByTransform();
        assertThat(list1.get(0).getArtists(), notNullValue());
        assertThat(list1.size(), is(3));
        assertThat(list1.get(0).getArtists().size(), greaterThan(0));
    }
    @Test
    public void findByListTest() {
        List<MusicFestival> list1 = service.findMusicFestivalByList();
        assertThat(list1.get(0).getArtists(), notNullValue());
        assertThat(list1.size(), is(3));
        assertThat(list1.get(0).getArtists().size(), greaterThan(0));
    }

    @Test
    public void findByReverseTest() {
        List<MusicFestival> list1 = service.findMusicFestivalByReverse();
        assertThat(list1.get(0).getArtists(), notNullValue());
        assertThat(list1.get(0).getArtists().size(), greaterThan(0));
        assertThat(list1.size(), is(3));
    }
    @Test
    public void findByBottomTest() {
        List<MemberInformation> list1 = service.findMusicFestivalByBottom();
        assertThat(list1.get(0).getArtist(), notNullValue());
        assertThat(list1.get(0).getArtist().getMembers().size(), greaterThan(0));
        assertThat(list1.size(), is(3));
    }

    @Test
    public void findByNestedListTest() {
        List<MusicFestival> list1 = service.findMusicFestivalByNestedList();
        assertThat(list1.get(0).getArtists(), notNullValue());
        assertThat(list1.get(0).getArtists().size(), greaterThan(0));
        assertThat(list1.size(), is(3));
    }

}