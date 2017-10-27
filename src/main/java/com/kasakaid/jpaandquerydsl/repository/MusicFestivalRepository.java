package com.kasakaid.jpaandquerydsl.repository;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.domain.QMusicFestival;
import com.kasakaid.jpaandquerydsl.domain.artist.QArtist;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.SQLQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MusicFestivalRepository {
    private final SQLQueryFactory sqlQueryFactory;

    private final EntityManager em;

    private QMusicFestival musicFestival = QMusicFestival.musicFestival;
    private QMusicFestival m = new QMusicFestival("m");
    private QArtist artist = QArtist.artist;
    private QArtist a = new QArtist("a");

    public List<MusicFestival> findMusicFestival() {
        log.info("inner join");
        return sqlQueryFactory
                .select(musicFestival)
                .from(musicFestival)
                .leftJoin(artist)
                .on(musicFestival.id.eq(artist.id))
                .fetchResults()
                .getResults(); // join は無視される
    }

    public List<MusicFestival> findMusicFestivalWhereJoin() {
        log.info("where join");
        return sqlQueryFactory
                .select(musicFestival)
                .from(musicFestival, artist)
                .where(musicFestival.id.eq(artist.id))
                .fetchResults()
                .getResults();
//                .fetch(); // fetch で実行すると musicFestival という列を探しに行こうとする。
    }

    public List<MusicFestival> findMusicFestivalByJPAQuery() {
        log.info("jpa query");
        JPAQueryFactory qFactory = new JPAQueryFactory(em);
        // JPA で OneToMany と ManyToOne の関係が結ばれた列は on に入れると、エラーになる。
        // 関連性のない項目を連結させる。
        return qFactory.selectDistinct(musicFestival).from(musicFestival)
                .innerJoin(musicFestival.artists, a)
                .on(musicFestival.id.eq(Expressions.asNumber(1)))
//                .fetchJoin() with-clause not allowed on fetched associations
                .fetch();
    }
}
