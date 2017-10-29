package com.kasakaid.jpaandquerydsl.domain;

import com.kasakaid.jpaandquerydsl.domain.artist.Artist;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data //native query はフィールドインジェクションしない
@NoArgsConstructor
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
        this.artists = artists;
    }

    public MusicFestival(int festivalId, String festivalName, String place, LocalDate eventDate, List<Artist> artists) {
        this.festivalId = festivalId;
        this.festivalName = festivalName;
        this.place = place;
        this.eventDate = eventDate;
        this.artists = artists;
    }

    public MusicFestival(Artist artist) {
        artists.add(artist);
    }
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "musicFestival")
    @OrderBy("festivalId ASC")
    private List<Artist> artists = new LinkedList<>();
}
