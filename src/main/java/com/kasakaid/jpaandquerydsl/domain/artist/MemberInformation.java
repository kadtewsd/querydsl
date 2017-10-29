package com.kasakaid.jpaandquerydsl.domain.artist;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
public class MemberInformation implements Serializable {
    @Id
    private int memberId;

    private int artistId;

    private String memberName;

    private String instrumental;

    @ManyToOne
    @JoinColumn(name = "artistId", insertable = false, updatable = false)
    private Artist artist;

    public MemberInformation(int memberId, int artistId, String memberName, String instrumental) {
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
    }
}
