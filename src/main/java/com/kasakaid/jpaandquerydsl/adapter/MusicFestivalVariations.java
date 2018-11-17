package com.kasakaid.jpaandquerydsl.adapter;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.MappingProjection;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
@Slf4j
@RequiredArgsConstructor
public class MusicFestivalVariations {

    private final SQLQueryFactory sqlQueryFactory;
    private final EntityDSL dsl;
    private final EntityAdapter change;

    /**
     * ルートエンティティのみをコンストラクタにて構築する方法
     *
     * @return
     */
    public List<MusicFestival> findMusicFestival() {
        log.info("where join");
        return sqlQueryFactory
                .select(
                        Projections.constructor(MusicFestival.class,
                                dsl.mf.festivalId,
                                dsl.mf.festivalName,
                                dsl.mf.place,
                                dsl.mf.eventDate
                        ))
                .from(dsl.musicFestival.as(dsl.mf),
                        dsl.artist.as(dsl.a)
                )
                .where(dsl.mf.festivalId.eq(dsl.a.festivalId))
                .fetch();
    }

    /**
     * ルートエンティティ配下のエンティティを構築します。
     * 下記のように非常に煩雑な実装が必要になり、わかりずらくなります。
     * そのため、Query クラスを別途作成して、各エンティティを取得し、
     * Repository にて値オブジェクトとして組み立てるのが良いと思います。
     *
     * @return
     */
    public List<MusicFestival> findUglyMusicFestival() {
        List<MusicFestival> musicFestivals = new ArrayList<>();
        sqlQueryFactory
                .select(
                        new MappingProjection<MusicFestival>(
                                MusicFestival.class,
                                dsl.mf.festivalId,
                                dsl.mf.festivalName,
                                dsl.mf.place,
                                dsl.mf.eventDate,
                                dsl.a.festivalId,
                                dsl.a.artistId,
                                dsl.a.artistName,
                                dsl.m.memberId,
                                dsl.m.memberName,
                                dsl.m.instrumental
                        ) {

                            @Override
                            protected MusicFestival map(Tuple row) {
                                return change.musicFestivalMap(row, musicFestivals);
                            }
                        }
                )
                .from(dsl.musicFestival.as(dsl.mf),
                        dsl.artist.as(dsl.a),
                        dsl.member.as(dsl.m)
                )
                .where(
                        dsl.mf.festivalId.eq(dsl.a.festivalId)
                                .and(dsl.a.artistId.eq(dsl.m.artistId)))
                .orderBy(
                        new OrderSpecifier[]{
                                new OrderSpecifier(Order.ASC, dsl.mf.festivalId),
                                new OrderSpecifier(Order.ASC, dsl.a.artistId),
                                new OrderSpecifier(Order.ASC, dsl.m.memberId)
                        }
                )
                .fetch();
        return musicFestivals;
    }


    public List<MusicFestival> subQueryWithMax() {
        List<MusicFestival> musicFestivals = new ArrayList<>();
        sqlQueryFactory
                .select(
                        new MappingProjection<MusicFestival>(
                                MusicFestival.class,
                                dsl.mf.festivalId,
                                dsl.mf.festivalName,
                                dsl.mf.place,
                                dsl.mf.eventDate,
                                dsl.a.festivalId,
                                dsl.a.artistId,
                                dsl.a.artistName,
                                dsl.m.memberId,
                                dsl.m.memberName,
                                dsl.m.instrumental
                        ) {

                            @Override
                            protected MusicFestival map(Tuple row) {
                                return change.musicFestivalMap(row, musicFestivals);
                            }
                        }
                ).from(
                maxMusicFestival(), dsl.aliasMusicFestival()
        ).leftJoin(
                dsl.artist, dsl.aliasArtist()
        ).on(
                dsl.mf.festivalId.eq(dsl.a.festivalId)
        ).leftJoin(
                dsl.member, dsl.aliasMemberInformation()
        ).on(
                dsl.a.artistId.eq(dsl.m.artistId)
        )
                .fetch();

        return musicFestivals;
    }

    private SQLQuery<Tuple> maxMusicFestival() {
        return SQLExpressions
                .select(
                        dsl.musicFestival.festivalId,
                        dsl.musicFestival.festivalName,
                        dsl.musicFestival.place,
                        dsl.musicFestival.eventDate
                )
                .from(
                        dsl.musicFestival
                )
                .innerJoin(
                        SQLExpressions
                                .select(
                                        dsl.musicFestival.festivalId,
                                        dsl.musicFestival.eventDate.max().as(dsl.musicFestival.eventDate)
                                )
                                .from(
                                        dsl.musicFestival
                                )
                                .groupBy(
                                        dsl.musicFestival.festivalId
                                )
                        , dsl.aliasMusicFestival())
                .on(
                        dsl.musicFestival.festivalId.eq(dsl.mf.festivalId)
                                .and(dsl.musicFestival.eventDate.eq(dsl.mf.eventDate))
                );
    }
}
