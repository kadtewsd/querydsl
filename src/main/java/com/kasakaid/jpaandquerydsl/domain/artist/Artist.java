package com.kasakaid.jpaandquerydsl.domain.artist;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class Artist implements Serializable {

    // SqlQueryFactory では列名がかぶると SQL の重複エラーになる
    @Id
    private int artistId;

    private int festivalId;

    private String artistName;

    @QueryProjection
    public Artist(int artistId, int festivalId, String artistName) {
        this.artistId = artistId;
        this.festivalId = festivalId;
        this.artistName = artistName;
    }
    public Artist(int artistId, int festivalId, String artistName, MusicFestival musicFestival, Set<MemberInformation> memberInformation) {
        this.artistId = artistId;
        this.festivalId = festivalId;
        this.artistName = artistName;
        this.musicFestival = musicFestival;
        this.members = memberInformation;
    }

//    @Enumerated(EnumType.STRING)
//    private Genre genre;

//    private String artistType;

    @ManyToOne
    @JoinColumn(name = "festivalId", insertable = false, updatable = false)
    private MusicFestival musicFestival;

    @OneToMany(mappedBy = "artist")
    private Set<MemberInformation> members;

}
