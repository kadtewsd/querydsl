package com.kasakaid.jpaandquerydsl.domain.artist;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
//@DiscriminatorColumn(name = "artistType")
public abstract class Artist implements Serializable {

    Artist(int id, String name) {
        this.id = id;
        this.artistName = name;
    }

    @Id
    private int id;

    private String artistName;

//    @Enumerated(EnumType.STRING)
//    private Genre genre;

//    private String artistType;

    @ManyToOne
    @JoinColumn(name = "festival_Id", insertable = false, updatable = false)
    private MusicFestival musicFestival;
}
