package com.kasakaid.jpaandquerydsl.domain.artist;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSolo is a Querydsl query type for Solo
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSolo extends EntityPathBase<Solo> {

    private static final long serialVersionUID = -1516199411L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSolo solo = new QSolo("solo");

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

    public final EnumPath<Sex> sex = createEnum("sex", Sex.class);

    public QSolo(String variable) {
        this(Solo.class, forVariable(variable), INITS);
    }

    public QSolo(Path<? extends Solo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSolo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSolo(PathMetadata metadata, PathInits inits) {
        this(Solo.class, metadata, inits);
    }

    public QSolo(Class<? extends Solo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QArtist(type, metadata, inits);
        this.artistName = _super.artistName;
        this.artistType = _super.artistType;
        this.genre = _super.genre;
        this.id = _super.id;
        this.musicFestival = _super.musicFestival;
    }

}

