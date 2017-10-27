package com.kasakaid.jpaandquerydsl.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMusicFestival is a Querydsl query type for MusicFestival
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMusicFestival extends EntityPathBase<MusicFestival> {

    private static final long serialVersionUID = 1166941092L;

    public static final QMusicFestival musicFestival = new QMusicFestival("musicFestival");

    public final ListPath<com.kasakaid.jpaandquerydsl.domain.artist.Artist, com.kasakaid.jpaandquerydsl.domain.artist.QArtist> artists = this.<com.kasakaid.jpaandquerydsl.domain.artist.Artist, com.kasakaid.jpaandquerydsl.domain.artist.QArtist>createList("artists", com.kasakaid.jpaandquerydsl.domain.artist.Artist.class, com.kasakaid.jpaandquerydsl.domain.artist.QArtist.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> eventDate = createDate("eventDate", java.time.LocalDate.class);

    public final NumberPath<Integer> festivalId = createNumber("festivalId", Integer.class);

    public final StringPath festivalName = createString("festivalName");

    public final StringPath place = createString("place");

    public QMusicFestival(String variable) {
        super(MusicFestival.class, forVariable(variable));
    }

    public QMusicFestival(Path<? extends MusicFestival> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMusicFestival(PathMetadata metadata) {
        super(MusicFestival.class, metadata);
    }

}

