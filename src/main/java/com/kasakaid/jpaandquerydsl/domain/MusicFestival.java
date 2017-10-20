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
    private Long id;

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
