package com.kasakaid.jpaandquerydsl.adapter;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.domain.QMusicFestival;
import com.kasakaid.jpaandquerydsl.domain.artist.QArtist;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.SQLExpressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Repository
public class JpaMusicFestivalRepository {

    private final EntityManager em;

    private QMusicFestival musicFestival = QMusicFestival.musicFestival;
    private QMusicFestival mf = new QMusicFestival("mf");
    private QArtist a = new QArtist("artists");

    public List<MusicFestival> findMusicFestivalByJPAQuery() {
        log.info("jpa query");
        JPAQueryFactory qFactory = new JPAQueryFactory(em);
        // JPA で OneToMany と ManyToOne の関係が結ばれた列は on に入れると、エラーになる。
        // 関連性のない項目を連結させる。
        return qFactory.selectDistinct(musicFestival).from(musicFestival, mf)
                .innerJoin(musicFestival.artists, a)
                .on(musicFestival.festivalId.eq(Expressions.asNumber(1)))
//                .fetchJoin() with-clause not allowed on fetched associations
                .fetch();
    }


    public List<MusicFestival> findMusicFestivalByExists() {
        log.info("jpa query");
        JPAQueryFactory qFactory = new JPAQueryFactory(em);
        // JPA で OneToMany と ManyToOne の関係が結ばれた列は on に入れると、エラーになる。
        // 関連性のない項目を連結させる。
        return qFactory.selectDistinct(musicFestival)
                .from(musicFestival)
                .where(
                        SQLExpressions.select(Expressions.asNumber(1))
//                                .from(musicFestival, mf)
                                .from(mf)
                                .where(musicFestival.festivalId.eq(mf.festivalId))
                                .exists()
                )
                .fetch();
    }

    public List<MusicFestival> findMusicFestivalByNotExists() {
        log.info("jpa query");
        JPAQueryFactory qFactory = new JPAQueryFactory(em);
        // JPA で OneToMany と ManyToOne の関係が結ばれた列は on に入れると、エラーになる。
        // 関連性のない項目を連結させる。
        return qFactory.selectDistinct(musicFestival)
                .from(musicFestival)
                .where(
                        SQLExpressions.select(Expressions.ONE)
                                .from(mf)
//                                .from(musicFestival, mf) // これだと、cross join する。
                                .where(musicFestival.festivalId.eq(mf.festivalId))
                                .notExists()
                )
                .fetch();
    }
}
