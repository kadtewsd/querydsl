package com.kasakaid.jpaandquerydsl.domain;

import com.kasakaid.jpaandquerydsl.domain.artist.Artist;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "music_festival")
@AllArgsConstructor
@Builder(builderMethodName = "of")
public class MusicFestival {
    @Id
    @Getter
    private int id;

    @Getter
    private String name;

    @Getter
    private String place;

    @Getter
    private LocalDate eventDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "musicFestival")
    @Getter
    @Setter
    @OrderBy("id ASC")
    private List<Artist> artists;
}
