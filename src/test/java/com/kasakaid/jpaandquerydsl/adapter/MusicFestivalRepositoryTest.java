package com.kasakaid.jpaandquerydsl.adapter;

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

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

@SpringBootTest(classes = {Application.class, TestConfig.class})
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Slf4j
public class MusicFestivalRepositoryTest extends AbstractBaseTest {

    @SneakyThrows
    @Before
    public void setUp() {
        super.setup();
        myResource.insertData("music_festival");
    }

    /**
     * 最も細かい粒度となるメンバー数が取得されるレコード数になる。
     */
    int japanjam = 7, metrock = 3, rockin = 1;

    @Autowired
    private MusicFestivalRepository repository;
    @Autowired
    private NormalJpaRepository normalJpaRepository;
    @Autowired
    private JpaMusicFestivalRepository jpaMusicFestivalRepository;
    @Autowired
    private MusicFestivalDMLRepository dmlRepository;

    @Test
    public void selectJPAQueryFactory() {
        List<MusicFestival> list = normalJpaRepository.findAll();
        assertThat(list.size(), is(3));
        List<MusicFestival> list0 = jpaMusicFestivalRepository.findMusicFestivalByJPAQuery();
        assertThat(list0.get(0).getArtists(), notNullValue());
        assertThat(list0.get(0).getArtists().size(), is(6));
    }

    @Test
    public void leftJoinTableTest() {
        List<MusicFestival> list = normalJpaRepository.findAll();
        assertThat(list.size(), is(3));
        List<MusicFestival> list0 = repository.findMusicFestival();
        assertThat(list0.get(0).getArtists(), notNullValue());
        assertThat(list0.get(0).getArtists().size(), greaterThan(0));
        assertThat(list0.size(), is(allRecords()));
        list.forEach(x -> log.info(x.getFestivalName()));
    }
    @Test
    public void findByTransFormTest() {
        List<MusicFestival> list1 = repository.findMusicFestivalByTransform();
        assertThat(list1.get(0).getArtists(), notNullValue());
        assertThat(list1.size(), is(allRecords()));
        assertThat(list1.get(0).getArtists().size(), greaterThan(0));
    }
    @Test
    public void findByListTest() {
        List<MusicFestival> list1 = repository.findMusicFestivalByList();
        assertThat(list1.get(0).getArtists(), notNullValue());
        assertThat(list1.size(), is(japanjam + metrock + rockin));
        assertThat(list1.get(0).getArtists().size(), greaterThan(0));
    }

    @Test
    public void findByReverseTest() {
        List<MusicFestival> list1 = repository.findMusicFestivalByReverse();
        assertThat(list1.get(0).getArtists(), notNullValue());
        assertThat(list1.get(0).getArtists().size(), is(0));
        assertThat(list1.size(), is(allRecords()));
    }
    @Test
    public void findByBottomTest() {
        List<MemberInformation> list1 = repository.findMusicFestivalByBottom();
        assertThat(list1.get(0).getArtist(), notNullValue());
        assertThat(list1.get(0).getArtist().getMembers().size(), greaterThan(0));
        assertThat(list1.size(), is(allRecords()));
    }

    @Test
    public void findByNestedListTest() {
        // メンバー分が 1 つになる.
        List<MusicFestival> list1 = repository.findMusicFestivalByNestedList();
        assertThat(list1.get(0).getArtists(), notNullValue());
        assertThat(list1.get(0).getArtists().size(), greaterThan(0));
        assertThat(list1.size(), is(allRecords() - 3));
    }

    @Test
    public void findByExists() {
        List<MusicFestival> list1 = jpaMusicFestivalRepository.findMusicFestivalByExists();
        assertThat(list1.get(0).getArtists(), notNullValue());
        assertThat(list1.get(0).getArtists().size(), greaterThan(0));
        assertThat(list1.size(), is(3));
    }

    @Test
    public void findByNotExists() {
        List<MusicFestival> list1 = jpaMusicFestivalRepository.findMusicFestivalByNotExists();
        assertThat(list1.size(), is(0));
    }

    @Test
    public void testUpdate() {
        assertThat(dmlRepository.updateFestival(), is(2L));
    }

    @Test
    public void testUpdateIndividually() {
        assertThat(dmlRepository.updateFestivalIndividually(), is(2L));
    }

    int allRecords() {
        return rockin + metrock + japanjam;
    }
}