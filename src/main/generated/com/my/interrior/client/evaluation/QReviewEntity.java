package com.my.interrior.client.evaluation;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewEntity is a Querydsl query type for ReviewEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewEntity extends EntityPathBase<ReviewEntity> {

    private static final long serialVersionUID = -1872104809L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewEntity reviewEntity = new QReviewEntity("reviewEntity");

    public final StringPath RCategory = createString("RCategory");

    public final StringPath RContent = createString("RContent");

    public final StringPath RMainPhoto = createString("RMainPhoto");

    public final NumberPath<Long> RNo = createNumber("RNo", Long.class);

    public final StringPath RStarRating = createString("RStarRating");

    public final StringPath RTitle = createString("RTitle");

    public final NumberPath<Integer> RViews = createNumber("RViews", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> RWrittenTime = createDateTime("RWrittenTime", java.time.LocalDateTime.class);

    public final com.my.interrior.client.user.QUserEntity user;

    public QReviewEntity(String variable) {
        this(ReviewEntity.class, forVariable(variable), INITS);
    }

    public QReviewEntity(Path<? extends ReviewEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewEntity(PathMetadata metadata, PathInits inits) {
        this(ReviewEntity.class, metadata, inits);
    }

    public QReviewEntity(Class<? extends ReviewEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.my.interrior.client.user.QUserEntity(forProperty("user")) : null;
    }

}

