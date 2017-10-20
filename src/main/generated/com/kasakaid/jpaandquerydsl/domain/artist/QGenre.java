package com.kasakaid.jpaandquerydsl.domain.artist;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QGenre is a Querydsl query type for Genre
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QGenre extends BeanPath<Genre> {

    private static final long serialVersionUID = 231080469L;

    public static final QGenre genre = new QGenre("genre");

    public final StringPath name = createString("name");

    public final StringPath note = createString("note");

    public QGenre(String variable) {
        super(Genre.class, forVariable(variable));
    }

    public QGenre(Path<? extends Genre> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGenre(PathMetadata metadata) {
        super(Genre.class, metadata);
    }

}

