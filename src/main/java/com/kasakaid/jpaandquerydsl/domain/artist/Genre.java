package com.kasakaid.jpaandquerydsl.domain.artist;

import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Genre {

    private String name;
    private String note;
}
