package com.kasakaid.jpaandquerydsl.repository;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.domain.artist.Artist;
import com.kasakaid.jpaandquerydsl.domain.artist.MemberInformation;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ChangeDataToEntity {

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
}
