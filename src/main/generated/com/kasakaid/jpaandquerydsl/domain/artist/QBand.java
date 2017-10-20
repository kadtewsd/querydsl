package com.kasakaid.jpaandquerydsl.domain.artist;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBand is a Querydsl query type for Band
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBand extends EntityPathBase<Band> {

    private static final long serialVersionUID = -1516719261L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBand band = new QBand("band");

    public final QArtist _super;

    //inherited
    public final StringPath artistName;

    //inherited
    public final StringPath artistType;

    // inherited
    public final QGenre genre;

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final com.kasakaid.jpaandquerydsl.domain.QMusicFestival musicFestival;

    public QBand(String variable) {
        this(Band.class, forVariable(variable), INITS);
    }

    public QBand(Path<? extends Band> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBand(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBand(PathMetadata metadata, PathInits inits) {
        this(Band.class, metadata, inits);
    }

    public QBand(Class<? extends Band> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QArtist(type, metadata, inits);
        this.artistName = _super.artistName;
        this.artistType = _super.artistType;
        this.genre = _super.genre;
        this.id = _super.id;
        this.musicFestival = _super.musicFestival;
    }

}

