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

    Artist artist(Tuple row) {
        return new Artist(
                row.get(dsl.a.festivalId),
                row.get(dsl.a.artistId),
                row.get(dsl.a.artistName)
        );
    }

    MemberInformation memberInformation(Tuple row) {
        return new MemberInformation(
                row.get(dsl.a.artistId),
                row.get(dsl.m.memberId),
                row.get(dsl.m.memberName), row.get(dsl.m.instrumental)
        );
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
