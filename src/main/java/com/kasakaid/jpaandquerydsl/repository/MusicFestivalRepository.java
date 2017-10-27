package com.kasakaid.jpaandquerydsl.repository;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.domain.QMusicFestival;
import com.kasakaid.jpaandquerydsl.domain.artist.QArtist;
import com.kasakaid.jpaandquerydsl.domain.artist.QMemberInformation;
import com.querydsl.core.types.Projections;
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
    private QMusicFestival mf = new QMusicFestival("mf");
    private QArtist artist = QArtist.artist;
    private QArtist a = new QArtist("a");
    // 名前がかぶる場合、テーブル名はmember1 とかいう感じで解釈される。
    private QMemberInformation member = QMemberInformation.memberInformation;
    private QMemberInformation m = new QMemberInformation("m");

    public List<MusicFestival> findMusicFestival() {
        log.info("inner join");
        // SQLQueryFactory だと列名を指定する必要がある。
        return sqlQueryFactory
                .selectDistinct(
                        Projections.bean(MusicFestival.class,
                        mf.festivalId,
                        mf.festivalName,
                        mf.eventDate,
                        mf.place,
                        a.artistId,
                        a.artistName,
                        m.memberId,
                        m.memberName,
                        m.instrumental
                        ))
                .from(musicFestival.as(mf))
                .leftJoin(artist, a)
                .on(mf.festivalId.eq(a.festivalId))
                .leftJoin(member, m)
                .on(a.artistId.eq(m.artistId))
                .fetch();
    }

    public List<MusicFestival> findMusicFestivalWhereJoin() {
        log.info("where join");
        return sqlQueryFactory
                .selectDistinct(
                       Projections.bean(MusicFestival.class,
                               mf.festivalId,
                               mf.festivalName,
                               mf.eventDate,
                               mf.place,
                               a.artistId,
                               a.artistName,
                               m.memberId,
                               m.memberName,
                               m.instrumental
                        ))
                .from(musicFestival.as(mf), artist.as(a), member.as(m))
                .where(mf.festivalId.eq(a.festivalId))
                .where(a.artistId.eq(m.artistId))
                .fetchResults()
                .getResults();
//                .fetch(); // fetch で実行すると musicFestival という列を探しに行こうとする。
    }

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
}
