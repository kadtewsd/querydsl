package com.kasakaid.jpaandquerydsl.repository;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.domain.QMusicFestival;
import com.kasakaid.jpaandquerydsl.domain.artist.Artist;
import com.kasakaid.jpaandquerydsl.domain.artist.MemberInformation;
import com.kasakaid.jpaandquerydsl.domain.artist.QArtist;
import com.kasakaid.jpaandquerydsl.domain.artist.QMemberInformation;
import com.querydsl.core.QueryResults;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.SQLQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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
    private QArtist a = new QArtist("artists");
    // 名前がかぶる場合、テーブル名はmember1 とかいう感じで解釈される。
    private QMemberInformation member = QMemberInformation.memberInformation;
    private QMemberInformation m = new QMemberInformation("m");

    public List<MusicFestival> findMusicFestival() {
        log.info("left join");
        // SQLQueryFactory だと列名を指定する必要がある。

        QBean<MemberInformation> memberInformationBean = Projections.bean(MemberInformation.class,
                m.memberId,
                m.artistId,
                m.memberName,
                m.instrumental
        );

        QBean<Artist> artistBean = Projections.bean(Artist.class,
                        a.artistId,
                        a.festivalId,
                        a.artistName,
                GroupBy.set(memberInformationBean).as("member")
        );
        QBean<MusicFestival> bean = Projections.bean(MusicFestival.class,
                mf.festivalId,
                mf.festivalName,
                mf.eventDate,
                mf.place,
                Projections.bean(Artist.class,
                        a.artistId.as("artists.artistId"),
                        a.festivalId,
                        a.artistName
                ).as("artistsR"),
                m.memberId.as(m.memberId),
                m.memberName.as(m.memberName),
                m.instrumental.as(m.instrumental),
            GroupBy.set(artistBean).as("artist")
        );

        // 色々やってみたが、QueryDSL でのマッピングは最上位のクラスにのみ行うらしい。
        // ネストしているListにはマッピングしてくれる気配がない。
        // そのため、ドメインモデルを構築するためのDTO的なクラスが必要になる。
        return sqlQueryFactory.from(musicFestival)
                .from(musicFestival.as(mf))
                .leftJoin(artist, a)
                .on(mf.festivalId.eq(a.festivalId))
                .leftJoin(member, m)
                .on(a.artistId.eq(m.artistId))
                .transform(GroupBy.groupBy(artistBean).list(bean))
                .stream().distinct().collect(Collectors.toList());
//        return sqlQueryFactory
//                .selectDistinct(
//                        Projections.bean(musicFestival,
//                        mf.festivalId,
//                        mf.festivalName,
//                        mf.eventDate,
//                        mf.place,
//                        a.artistId,
//                        a.artistName
//                        m.memberId.as(m.memberId),
//                        m.memberName.as(m.memberName),
//                        m.instrumental.as(m.instrumental)
//                        ))
//                .from(musicFestival.as(mf))
//                .leftJoin(artist, a)
//                .on(mf.festivalId.eq(a.festivalId))
//                .leftJoin(member, m)
//                .on(a.artistId.eq(m.artistId))
//                .fetch().stream().distinct().collect(Collectors.toList());
    }

    public <T extends Serializable> List<MusicFestival> findMusicFestivalWhereJoin() {
        log.info("where join");
        QueryResults<MusicFestival> results = sqlQueryFactory
                .selectDistinct(
                       Projections.bean(MusicFestival.class,
                               mf.festivalId,
                               mf.festivalName,
                               mf.eventDate,
                               mf.place,
                               a.artistId,
                               m.memberId,
                               m.memberName,
                               m.instrumental
                        ))
                .from(musicFestival.as(mf), artist.as(a), member.as(m))
                .where(mf.festivalId.eq(a.festivalId))
                .where(a.artistId.eq(m.artistId))
//                .fetch();
                .fetchResults();
//                .getResults().stream().distinct().collect(Collectors.toList());
        return results.getResults();
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
