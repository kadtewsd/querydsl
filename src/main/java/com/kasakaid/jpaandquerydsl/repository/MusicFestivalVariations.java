package com.kasakaid.jpaandquerydsl.repository;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.domain.artist.Artist;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.MappingProjection;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
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
    private final ChangeDataToEntity change;

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
                                return musicFestivalMap(row, musicFestivals);
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

    /**
     * SQL の取得結果を stream を使って、エンティティに組み立てます。
     * 各エンティティのリストに存在する場合、存在しない場合を書いていくので、
     * 複雑なストリームになってしまいます。
     * また、別にイミュータブルにしている訳ではないのでメリットも低いです。
     * reduce をつかってやるべきなのでしょう。
     * @param row
     * @param musicFestivals
     * @return
     */
    MusicFestival musicFestivalMap(Tuple row, List<MusicFestival> musicFestivals) {
        musicFestivals.stream().filter(x -> x.getFestivalId() == row.get(dsl.mf.festivalId))
                .map(musicFestival -> {
                    musicFestival.getArtists().stream()
                            .filter(artist -> artist.getArtistId() == row.get(dsl.a.artistId))
                            .map(x1 -> {
                                // アーティストに新規のメンバーが追加された
                                x1.getMembers().add(change.memberInformation(row));
                                return change.memberInformation(row);
                            })
                            .findFirst()
                            .orElseGet(() -> {
                                // ミュージックフェスティバルに新規のアーティストが追加された
                                Artist newArtist = change.artist(row);
                                newArtist.getMembers().add(change.memberInformation(row));
                                musicFestival.getArtists().add(newArtist);
                                return change.memberInformation(row);
                            });
                    return change.musicFestival(row);
                })
                .findFirst()
                .orElseGet(() -> {
                    // 新規の音楽フェスティバルが取得された
                    MusicFestival newMf = change.musicFestival(row);
                    Artist artist = change.artist(row);
                    artist.getMembers().add(change.memberInformation(row));
                    newMf.getArtists().add(artist);
                    musicFestivals.add(newMf);
                    return change.musicFestival(row);
                });

        return change.musicFestival(row);
    }
}
