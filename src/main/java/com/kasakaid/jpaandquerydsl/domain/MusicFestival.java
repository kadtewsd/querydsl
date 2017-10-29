package com.kasakaid.jpaandquerydsl.domain;

import com.kasakaid.jpaandquerydsl.domain.artist.Artist;
import com.kasakaid.jpaandquerydsl.domain.artist.MemberInformation;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class MusicFestival implements Serializable {
    @Id
    private int festivalId;

    private String festivalName;

    private String place;

    private LocalDate eventDate;

    @QueryProjection
    public MusicFestival(int festivalId, String festivalName, String place, LocalDate eventDate) {
        this.festivalId = festivalId;
        this.festivalName = festivalName;
        this.place = place;
        this.eventDate = eventDate;
    }

    public MusicFestival(int festivalId, String festivalName, String place, LocalDate eventDate, Artist artist) {
        this.festivalId = festivalId;
        this.festivalName = festivalName;
        this.place = place;
        this.eventDate = eventDate;
        this.artists = new LinkedList<>();
        artists.add(artist);
    }

    public MusicFestival(int festivalId, String festivalName, String place, LocalDate eventDate, List<Artist> artists) {
        this.festivalId = festivalId;
        this.festivalName = festivalName;
        this.place = place;
        this.eventDate = eventDate;
        this.artists = artists;
    }

    public MusicFestival(int festivalId, String festivalName, String place, LocalDate eventDate, List<Artist> artists, List<MemberInformation> members) {
        this.festivalId = festivalId;
        this.festivalName = festivalName;
        this.place = place;
        this.eventDate = eventDate;
        this.artists = artists;
    }
    @QueryProjection
    public MusicFestival(int festivalId, String festivalName, String place,  LocalDate eventDate, Artist artist, MemberInformation memberInformation) {
        this.festivalId = festivalId;
        this.festivalName = festivalName;
        this.place = place;
        this.eventDate = eventDate;
        this.artists =  new LinkedList<>();
        this.artists.add(artist);
    }

    public MusicFestival(Artist artist) {
        artists.add(artist);
    }
    //SqlQueryFactory においても、fetch 属性は影響がある。デフォルトは Lazy になる。
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "musicFestival")
    @OrderBy("festivalId ASC")
    private List<Artist> artists = new LinkedList<>();
}
