package com.kasakaid.jpaandquerydsl.domain.artist;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArtist is a Querydsl query type for Artist
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QArtist extends EntityPathBase<Artist> {

    private static final long serialVersionUID = -1586038539L;

    public static ConstructorExpression<Artist> create(Expression<Integer> festivalId, Expression<Integer> artistId, Expression<String> artistName, Expression<? extends MemberInformation> memberInformation) {
        return Projections.constructor(Artist.class, new Class<?>[]{int.class, int.class, String.class, MemberInformation.class}, festivalId, artistId, artistName, memberInformation);
    }

    public static ConstructorExpression<Artist> create(Expression<Integer> festivalId, Expression<Integer> artistId, Expression<String> artistName) {
        return Projections.constructor(Artist.class, new Class<?>[]{int.class, int.class, String.class}, festivalId, artistId, artistName);
    }

    public static ConstructorExpression<Artist> create(NumberExpression<Integer> festivalId, NumberExpression<Integer> artistId, StringExpression artistName, Expression<? extends java.util.Set<MemberInformation>> members) {
        return Projections.constructor(Artist.class, new Class<?>[]{int.class, int.class, String.class, java.util.Set.class}, festivalId, artistId, artistName, members);
    }

    public static ConstructorExpression<Artist> create(NumberExpression<Integer> festivalId, NumberExpression<Integer> artistId, StringExpression artistName, com.kasakaid.jpaandquerydsl.domain.QMusicFestival musicFestival) {
        return Projections.constructor(Artist.class, new Class<?>[]{int.class, int.class, String.class, com.kasakaid.jpaandquerydsl.domain.MusicFestival.class}, festivalId, artistId, artistName, musicFestival);
    }

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArtist artist = new QArtist("artist");

    public final NumberPath<Integer> artistId = createNumber("artistId", Integer.class);

    public final StringPath artistName = createString("artistName");

    public final NumberPath<Integer> festivalId = createNumber("festivalId", Integer.class);

    public final SetPath<MemberInformation, QMemberInformation> members = this.<MemberInformation, QMemberInformation>createSet("members", MemberInformation.class, QMemberInformation.class, PathInits.DIRECT2);

    public final com.kasakaid.jpaandquerydsl.domain.QMusicFestival musicFestival;

    public QArtist(String variable) {
        this(Artist.class, forVariable(variable), INITS);
    }

    public QArtist(Path<? extends Artist> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArtist(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArtist(PathMetadata metadata, PathInits inits) {
        this(Artist.class, metadata, inits);
    }

    public QArtist(Class<? extends Artist> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.musicFestival = inits.isInitialized("musicFestival") ? new com.kasakaid.jpaandquerydsl.domain.QMusicFestival(forProperty("musicFestival")) : null;
    }

}

