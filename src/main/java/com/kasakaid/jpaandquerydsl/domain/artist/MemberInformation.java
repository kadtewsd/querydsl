package com.kasakaid.jpaandquerydsl.domain.artist;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
public class MemberInformation implements Serializable {
    @Id
    private Integer memberId;

    private Integer artistId;

    private String memberName;

    private String instrumental;

    @ManyToOne
    @JoinColumn(name = "artistId", insertable = false, updatable = false)
    private Artist artist;

    @QueryProjection
    public MemberInformation(Integer artistId, Integer memberId,  String memberName, String instrumental) {
        this.artistId = artistId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.instrumental = instrumental;
    }
    public MemberInformation(int memberId, int artistId, String memberName, String instrumental, Artist artist) {
        this.artistId = artistId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.instrumental = instrumental;
        this.artist = artist;
        this.artist.generateMembers();
        this.artist.getMembers().add(this);
    }
}
