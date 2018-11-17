package com.kasakaid.jpaandquerydsl.adapter;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.domain.QMusicFestival;
import com.kasakaid.jpaandquerydsl.domain.artist.Artist;
import com.kasakaid.jpaandquerydsl.domain.artist.MemberInformation;
import com.kasakaid.jpaandquerydsl.domain.artist.QArtist;
import com.kasakaid.jpaandquerydsl.domain.artist.QMemberInformation;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QList;
import com.querydsl.sql.SQLQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// リポジトリを直接コールすると例外が発生する。あえて、@Transactional をつける。
// @Service のクラスだけにアノテーションをつけてもでトランザクションが開始することは確認できた。
@Transactional
@Repository
@RequiredArgsConstructor
@Slf4j
/**
 * このクラスの実装だと最も細かい粒度のエンティティの数だけレコードが生成される。
 * CQRS でリードモデルを作るときは問題ないが、エンティティを作ろうとすると非常に問題がある実装
 */
public class MusicFestivalRepository {
    private final SQLQueryFactory sqlQueryFactory;

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

        return sqlQueryFactory
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

    public List<MusicFestival> findMusicFestivalByReverse() {

        ConstructorExpression<MusicFestival> musicFestivalConstructorExpression = Projections.constructor(MusicFestival.class,
                mf.festivalId,
                mf.festivalName,
                mf.place,
                mf.eventDate
        );

        ConstructorExpression<Artist> artistConstructor = Projections.constructor(Artist.class,
                a.festivalId.as(a.festivalId),
                a.artistId,
                a.artistName,
                musicFestivalConstructorExpression
        );

        ConstructorExpression<MemberInformation> memberInformationConstructorExpression = Projections.constructor(MemberInformation.class,
                m.artistId.as(m.artistId),
                m.memberId,
                m.memberName,
                m.instrumental,
                artistConstructor
        );

        return sqlQueryFactory
                .from(musicFestival.as(mf))
                .leftJoin(artist, a)
                .on(mf.festivalId.eq(a.festivalId))
                .leftJoin(member, m)
                .on(a.artistId.eq(m.artistId))
                // Projections.bean は setter で値を設定するので、ルートのエンティティにネストしたクラスのメンバーを定義する必要があるので、DTO を作る必要がある。
                // Projections.constructor では、DTO は作る必要はないように思うが、取得結果を基に、ドメインモデルは自力で構築する必要があるようだ。。。
                .transform(GroupBy.groupBy(memberInformationConstructorExpression)
                        .list(musicFestivalConstructorExpression))
                ;
    }

    public List<MusicFestival> findMusicFestivalByNestedList() {
        // list で射影すると unmodifiableList というよくわからない型で返ってくる。
        ConstructorExpression<MusicFestival> projection = Projections.constructor(MusicFestival.class,
                mf.festivalId,
                mf.festivalName,
                mf.place,
                mf.eventDate,
                Projections.list(
                        a.festivalId.as(a.festivalId),
                        a.artistId,
                        a.artistName,
                        Projections.list(
                                m.artistId.as(m.artistId),
                                m.memberId,
                                m.memberName,
                                m.instrumental
                        )
                )
        );

        ConstructorExpression<MusicFestival> musicFestivalConstructorExpression = Projections.constructor(MusicFestival.class,
                mf.festivalId,
                mf.festivalName,
                mf.place,
                mf.eventDate
        );

        ConstructorExpression<Artist> artistConstructor = Projections.constructor(Artist.class,
                a.festivalId.as(a.festivalId),
                a.artistId,
                a.artistName,
                musicFestivalConstructorExpression
        );

        return sqlQueryFactory
                .from(musicFestival.as(mf))
                .leftJoin(artist, a)
                .on(mf.festivalId.eq(a.festivalId))
                .leftJoin(member, m)
                .on(a.artistId.eq(m.artistId))
                // Projections.bean は setter で値を設定するので、ルートのエンティティにネストしたクラスのメンバーを定義する必要があるので、DTO を作る必要がある。
                // Projections.constructor では、DTO は作る必要はないように思うが、取得結果を基に、ドメインモデルは自力で構築する必要があるようだ。。。
                .transform(GroupBy.groupBy(mf.festivalId, a.artistId)
                        .list(projection))
                ;
    }

    public List<MemberInformation> findMusicFestivalByBottom() {

        ConstructorExpression<MusicFestival> musicFestivalConstructorExpression = Projections.constructor(MusicFestival.class,
                mf.festivalId,
                mf.festivalName,
                mf.place,
                mf.eventDate
        );

        ConstructorExpression<Artist> artistConstructor = Projections.constructor(Artist.class,
                a.festivalId.as(a.festivalId),
                a.artistId,
                a.artistName,
                musicFestivalConstructorExpression
        );

        ConstructorExpression<MemberInformation> memberInformationConstructorExpression = Projections.constructor(MemberInformation.class,
                m.artistId.as(m.artistId),
                m.memberId,
                m.memberName,
                m.instrumental,
                artistConstructor
        );

        return sqlQueryFactory
                .from(musicFestival.as(mf))
                .leftJoin(artist, a)
                .on(mf.festivalId.eq(a.festivalId))
                .leftJoin(member, m)
                .on(a.artistId.eq(m.artistId))
                // Projections.bean は setter で値を設定するので、ルートのエンティティにネストしたクラスのメンバーを定義する必要があるので、DTO を作る必要がある。
                // Projections.constructor では、DTO は作る必要はないように思うが、取得結果を基に、ドメインモデルは自力で構築する必要があるようだ。。。
                .transform(GroupBy.groupBy(memberInformationConstructorExpression)
                        .list(memberInformationConstructorExpression))
                ;
    }

}
