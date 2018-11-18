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
 * QMemberInformation is a Querydsl query type for MemberInformation
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMemberInformation extends EntityPathBase<MemberInformation> {

    private static final long serialVersionUID = -575571004L;

    public static ConstructorExpression<MemberInformation> create(Expression<Integer> artistId, Expression<Integer> memberId, Expression<String> memberName, Expression<String> instrumental) {
        return Projections.constructor(MemberInformation.class, new Class<?>[]{int.class, int.class, String.class, String.class}, artistId, memberId, memberName, instrumental);
    }

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberInformation memberInformation = new QMemberInformation("memberInformation");

    public final QArtist artist;

    public final NumberPath<Integer> artistId = createNumber("artistId", Integer.class);

    public final StringPath instrumental = createString("instrumental");

    public final NumberPath<Integer> memberId = createNumber("memberId", Integer.class);

    public final StringPath memberName = createString("memberName");

    public QMemberInformation(String variable) {
        this(MemberInformation.class, forVariable(variable), INITS);
    }

    public QMemberInformation(Path<? extends MemberInformation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberInformation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberInformation(PathMetadata metadata, PathInits inits) {
        this(MemberInformation.class, metadata, inits);
    }

    public QMemberInformation(Class<? extends MemberInformation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.artist = inits.isInitialized("artist") ? new QArtist(forProperty("artist"), inits.get("artist")) : null;
    }

}

