package com.kasakaid.jpaandquerydsl.repository;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.domain.QMusicFestival;
import com.kasakaid.jpaandquerydsl.domain.artist.QArtist;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.SQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class MusicFestivalRepository {
    private final SQLQueryFactory sqlQueryFactory;

    private final EntityManager em;

    private QMusicFestival musicFestival = QMusicFestival.musicFestival;
    private QArtist artist = QArtist.artist;
    private QArtist a = new QArtist("a");

    public List<MusicFestival> findMusicFestival() {
       return sqlQueryFactory.selectDistinct(musicFestival).from(musicFestival)
               .innerJoin(artist).fetchResults().getResults();
    }
    public List<MusicFestival> findMusicFestivalByJPAQuery() {
        JPAQueryFactory qFactory = new JPAQueryFactory(em);
        // JPA で OneToMany と ManyToOne の関係が結ばれた列は on に入れると、エラーになる。
        // 関連性のない項目を連結させる。
        return qFactory.selectDistinct(musicFestival).from(musicFestival)
                .innerJoin(musicFestival.artists, a)
                .on(musicFestival.id.eq(Expressions.asNumber(1))).fetch();
    }
}
