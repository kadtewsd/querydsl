package com.kasakaid.jpaandquerydsl.domain.artist;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@DiscriminatorValue("band")
public class Band extends Artist {
    public Band(int id, String name) {
        super(id, name);
    }
}
