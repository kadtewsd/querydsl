package com.kasakaid.jpaandquerydsl.domain;

import com.kasakaid.jpaandquerydsl.domain.artist.Artist;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder(builderMethodName = "of")
public class MusicFestival {
    @Id
    @Getter
    private int festivalId;

    @Getter
    private String festivalName;

    @Getter
    private String place;

    @Getter
    private LocalDate eventDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "musicFestival")
    @Getter
    @Setter
    @OrderBy("festivalId ASC")
    private List<Artist> artists;
}
