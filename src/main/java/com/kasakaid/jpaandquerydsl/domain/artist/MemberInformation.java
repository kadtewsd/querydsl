package com.kasakaid.jpaandquerydsl.domain.artist;

import javax.persistence.*;

@Entity
public class MemberInformation {
    @Id
    private int memberId;

    private int artistId;

    private String memberName;

    private String instrumental;

    @ManyToOne
    @JoinColumn(name = "artistId", insertable = false, updatable = false)
    private Artist artist;
}
