package com.kasakaid.jpaandquerydsl.repository;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.domain.QMusicFestival;
import com.kasakaid.jpaandquerydsl.domain.artist.Artist;
import com.kasakaid.jpaandquerydsl.domain.artist.MemberInformation;
import com.kasakaid.jpaandquerydsl.domain.artist.QArtist;
import com.kasakaid.jpaandquerydsl.domain.artist.QMemberInformation;
import com.querydsl.core.QueryResults;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.*;
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

        // 色々やってみたが、QueryDSL でのマッピングは最上位のクラスにのみ行うらしい。
        // ネストしているListにはマッピングしてくれる気配がない。
        // そのため、ドメインモデルを構築するためのDTO的なクラスが必要になる。
        // Projections.bean は setter を使うが、Projections.constructor はコンストラクタを使うので、
        // コンストラクタから構築する。
        //  3.2.2. Bean population 3.2.3. Constructor usage
        // http://www.querydsl.com/static/querydsl/latest/reference/html/ch03s02.html
        return sqlQueryFactory.selectDistinct(
                Projections.constructor(MusicFestival.class,
                        mf.festivalId,
                        mf.festivalName,
                        mf.place,
                        mf.eventDate,
                        Projections.constructor(Artist.class,
                                a.festivalId.as(a.festivalId),
                                a.artistId,
                                a.artistName,
                                Projections.constructor(MemberInformation.class,
                                        m.artistId.as(m.artistId),
                                        m.memberId,
                                        m.memberName,
                                        m.instrumental)
                        )
                )
        )
                .from(musicFestival.as(mf))
                .leftJoin(artist, a)
                .on(mf.festivalId.eq(a.festivalId))
                .leftJoin(member, m)
                .on(a.artistId.eq(m.artistId))
                .fetch()
                ;
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

    public List<MusicFestival> findMusicFestivalByTransform() {

        ConstructorExpression<MemberInformation> memberInformationConstructorExpression = Projections.constructor(MemberInformation.class,
                m.artistId.as(m.artistId),
                m.memberId,
                m.memberName,
                m.instrumental
        );

        ConstructorExpression<Artist> artistConstructor = Projections.constructor(Artist.class,
                a.festivalId.as(a.festivalId),
                a.artistId,
                a.artistName,
                memberInformationConstructorExpression
        );

        ConstructorExpression<MusicFestival> musicFestivalConstructorExpression = Projections.constructor(MusicFestival.class,
                mf.festivalId,
                mf.festivalName,
                mf.place,
                mf.eventDate,
                artistConstructor
        );

        return sqlQueryFactory.selectDistinct(musicFestivalConstructorExpression)
                .from(musicFestival.as(mf))
                .leftJoin(artist, a)
                .on(mf.festivalId.eq(a.festivalId))
                .leftJoin(member, m)
                .on(a.artistId.eq(m.artistId))
        // Projections.bean は setter で値を設定するので、ルートのエンティティにネストしたクラスのメンバーを定義する必要があるので、DTO を作る必要がある。
        // Projections.constructor では、DTO は作る必要はないように思うが、取得結果を基に、ドメインモデルは自力で構築する必要があるようだ。。。
                .transform(GroupBy.groupBy(musicFestivalConstructorExpression)
                        .list(musicFestivalConstructorExpression))
                ;

    }
    public List<MusicFestival> findMusicFestivalByList() {
        QList memberList = Projections.list(
                m.artistId.as(m.artistId),
                m.memberId,
                m.memberName,
                m.instrumental
        );
        QList artistList = Projections.list(
                a.festivalId.as(a.festivalId),
                a.artistId,
                a.artistName,
                memberList
        );

        ConstructorExpression<MusicFestival> musicFestivalConstructorExpression = Projections.constructor(MusicFestival.class,
                mf.festivalId,
                mf.festivalName,
                mf.place,
                mf.eventDate,
                artistList
        );

        return sqlQueryFactory.selectDistinct(musicFestivalConstructorExpression)
                .from(musicFestival.as(mf))
                .leftJoin(artist, a)
                .on(mf.festivalId.eq(a.festivalId))
                .leftJoin(member, m)
                .on(a.artistId.eq(m.artistId))
                // Projections.list にて射影をした場合、transform にてネストしたリストにnullの値を設定されないようにする。
                // なお、Projections.list を指定した場合においても、List<Artist> のコンストラクタは動作するが、List<Artist> 、
                // の各業に都合よく Set<Members> が設定されているようなことはない。
                .transform(GroupBy.groupBy(musicFestivalConstructorExpression)
                        .list(musicFestivalConstructorExpression))
                ;

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
