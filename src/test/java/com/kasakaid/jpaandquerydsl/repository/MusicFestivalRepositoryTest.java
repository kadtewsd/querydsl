package com.kasakaid.jpaandquerydsl.repository;

import com.kasakaid.jpaandquerydsl.AbstractBaseTest;
import com.kasakaid.jpaandquerydsl.Application;
import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.spring.TestConfig;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

@SpringBootTest(classes = {Application.class, TestConfig.class})
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class MusicFestivalRepositoryTest extends AbstractBaseTest {

    @SneakyThrows
    @Before
    public void setUp() {
        super.setup();
        myResource.insertData("music_festival");
    }

    @Autowired
    private MusicFestivalRepository repository;
    @Autowired
    private NormalJpaRepository normalJpaRepository;

    @Test
    public void selectJPAQueryFactory() {
        List<MusicFestival> list0 = repository.findMusicFestivalByJPAQuery();
        assertThat(list0.size(), not(0));
        List<MusicFestival> list = normalJpaRepository.findAll();
        assertThat(list.size(), not(0));
    }

    @Test
    public void selectSqlQueryFactory() {
        List<MusicFestival> list = normalJpaRepository.findAll();
        assertThat(list.size(), not(0));
        List<MusicFestival> list0 = repository.findMusicFestival();
        assertThat(list0.size(), not(0));
    }

    @Test
    public void JoinTableTest() {
        List<MusicFestival> list1 = repository.findMusicFestivalWhereJoin();
        assertThat(list1.size(), not(0));
    }
}