package com.my.interrior.client.evaluation;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewCommentEntity is a Querydsl query type for ReviewCommentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewCommentEntity extends EntityPathBase<ReviewCommentEntity> {

    private static final long serialVersionUID = 820640238L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewCommentEntity reviewCommentEntity = new QReviewCommentEntity("reviewCommentEntity");

    public final StringPath rComment = createString("rComment");

    public final DateTimePath<java.time.LocalDateTime> rCommentCreated = createDateTime("rCommentCreated", java.time.LocalDateTime.class);

    public final NumberPath<Long> rCommentNo = createNumber("rCommentNo", Long.class);

    public final QReviewEntity reviewEntity;

    public final com.my.interrior.client.user.QUserEntity user;

    public QReviewCommentEntity(String variable) {
        this(ReviewCommentEntity.class, forVariable(variable), INITS);
    }

    public QReviewCommentEntity(Path<? extends ReviewCommentEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewCommentEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewCommentEntity(PathMetadata metadata, PathInits inits) {
        this(ReviewCommentEntity.class, metadata, inits);
    }

    public QReviewCommentEntity(Class<? extends ReviewCommentEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reviewEntity = inits.isInitialized("reviewEntity") ? new QReviewEntity(forProperty("reviewEntity"), inits.get("reviewEntity")) : null;
        this.user = inits.isInitialized("user") ? new com.my.interrior.client.user.QUserEntity(forProperty("user")) : null;
    }

}

