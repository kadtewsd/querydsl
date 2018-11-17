package com.kasakaid.jpaandquerydsl.domain.artist;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
public class Artist implements Serializable {

    // SqlQueryFactory では列名がかぶると SQL の重複エラーになる
    @Id
    private int artistId;

    private int festivalId;

    private String artistName;

    @QueryProjection
    public Artist(int festivalId, int artistId, String artistName, Set<MemberInformation> members) {
        this.artistId = artistId;
        this.festivalId = festivalId;
        this.artistName = artistName;
        this.members = members;
    }

    @QueryProjection
    public Artist(int festivalId, int artistId, String artistName) {
        this.artistId = artistId;
        this.festivalId = festivalId;
        this.artistName = artistName;
    }
    @QueryProjection
    public Artist(int festivalId, int artistId, String artistName, MemberInformation memberInformation) {
        this.artistId = artistId;
        this.festivalId = festivalId;
        this.artistName = artistName;
        this.members = new LinkedHashSet<>();
        this.members.add(memberInformation);
    }

    @QueryProjection
    public Artist(int festivalId, int artistId, String artistName, MusicFestival musicFestival) {
        this.artistId = artistId;
        this.festivalId = festivalId;
        this.artistName = artistName;
        this.musicFestival = musicFestival;
        musicFestival.generateArtists();
        this.musicFestival.getArtists().add(this);
    }

    public Artist(MemberInformation memberInformation) {
        this.members = new LinkedHashSet<>();
        members.add(memberInformation);
    }

    @ManyToOne
    @JoinColumn(name = "festivalId", insertable = false, updatable = false)
    private MusicFestival musicFestival;

    @OneToMany(mappedBy = "artist",fetch = FetchType.EAGER)
    private Set<MemberInformation> members = new HashSet<>();

    void generateMembers() {
        if (this.members == null) {
            members = new HashSet<>();
        }
    }
}
