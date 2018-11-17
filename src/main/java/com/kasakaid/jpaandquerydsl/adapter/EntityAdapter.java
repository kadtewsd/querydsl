package com.kasakaid.jpaandquerydsl.adapter;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.domain.artist.Artist;
import com.kasakaid.jpaandquerydsl.domain.artist.MemberInformation;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class EntityAdapter {

    private final EntityDSL dsl;

    MusicFestival musicFestival(Tuple row) {
        return new MusicFestival(
                row.get(dsl.mf.festivalId),
                row.get(dsl.mf.festivalName),
                row.get(dsl.mf.place),
                row.get(dsl.mf.eventDate)
        );
    }

    /**
     * left join で null になりうる項目を QueryDSL の row.get で取得後にいきなりインスタンスのコンストラクタに渡すと、
     * intelliJ の警告通り、NullPointerException が発生します。
     * なぜかというと、Artist エンティティのコンストラクタの artistId などが int というプリミティブ型で宣言されているからです。
     * ラッパークラスの値が null の場合、プリミティブに値を渡すとオートボクシングの際、NullPointExeception が発生します。
     * 本来このような事態を避けるため、エンティティの引数の型は、primitive からラッパークラスに変更すべきですが、備忘録のため、このひどい実装を残します。
     * 一度、ラッパーの変数に格納して、Null か判断して、NullPointerException を回避する、という力技です。
     *
     * @param row
     * @return
     */
    Artist artist(Tuple row) {
        Integer artistId = row.get(dsl.a.artistId);
        if (artistId == null) return new Artist();
        return new Artist(
                row.get(dsl.a.festivalId),
                artistId,
                row.get(dsl.a.artistName)
        );
    }

    MemberInformation memberInformation(Tuple row) {
        return new MemberInformation(
                row.get(dsl.a.artistId),
                row.get(dsl.m.memberId),
                row.get(dsl.m.memberName),
                row.get(dsl.m.instrumental)
        );
    }

    /**
     * SQL の取得結果を stream を使って、エンティティに組み立てます。
     * 各エンティティのリストに存在する場合、存在しない場合を書いていくので、
     * 複雑なストリームになってしまいます。
     * また、別にイミュータブルにしている訳ではないのでメリットも低いです。
     * reduce をつかってやるべきなのでしょう。
     *
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
                                x1.getMembers().add(this.memberInformation(row));
                                return this.memberInformation(row);
                            })
                            .findFirst()
                            .orElseGet(() -> {
                                // ミュージックフェスティバルに新規のアーティストが追加された
                                Artist newArtist = this.artist(row);
                                newArtist.getMembers().add(this.memberInformation(row));
                                musicFestival.getArtists().add(newArtist);
                                return this.memberInformation(row);
                            });
                    return this.musicFestival(row);
                })
                .findFirst()
                .orElseGet(() -> {
                    // 新規の音楽フェスティバルが取得された
                    MusicFestival newMf = this.musicFestival(row);
                    Artist artist = this.artist(row);
                    artist.getMembers().add(this.memberInformation(row));
                    newMf.getArtists().add(artist);
                    musicFestivals.add(newMf);
                    return this.musicFestival(row);
                });

        return this.musicFestival(row);
    }
}
