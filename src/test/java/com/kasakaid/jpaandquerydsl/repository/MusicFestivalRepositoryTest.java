package com.kasakaid.jpaandquerydsl.repository;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MusicFestivalRepositoryTest {

    @Autowired
    private MusicFestivalRepository repository;
    @Test
    public void selectJPAQueryFactory() {
        List<MusicFestival> list = repository.findMusicFestivalByJPAQuery();
        assertThat(list.size(), is(0));
    }

    @Test
    public void selectSqlQueryFactory() {
        List<MusicFestival> list = repository.findMusicFestival();
        assertThat(list.size(), is(0));
    }
}