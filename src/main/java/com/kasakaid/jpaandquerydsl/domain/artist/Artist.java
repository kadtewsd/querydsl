package com.kasakaid.jpaandquerydsl.domain.artist;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class Artist implements Serializable {

    Artist(int artistId, String name) {
        this.artistId = artistId;
        this.artistName = name;
    }

    // SqlQueryFactory では列名がかぶると SQL の重複エラーになる
    @Id
    private int artistId;

    private int festivalId;

    private String artistName;

//    @Enumerated(EnumType.STRING)
//    private Genre genre;

//    private String artistType;

    @ManyToOne
    @JoinColumn(name = "festivalId", insertable = false, updatable = false)
    private MusicFestival musicFestival;

    @OneToMany(mappedBy = "artist")
    private Set<MemberInformation> members;

}
