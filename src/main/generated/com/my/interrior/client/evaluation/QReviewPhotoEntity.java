package com.my.interrior.client.evaluation;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewPhotoEntity is a Querydsl query type for ReviewPhotoEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewPhotoEntity extends EntityPathBase<ReviewPhotoEntity> {

    private static final long serialVersionUID = 572793473L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewPhotoEntity reviewPhotoEntity = new QReviewPhotoEntity("reviewPhotoEntity");

    public final QReviewEntity review;

    public final NumberPath<Long> RpNo = createNumber("RpNo", Long.class);

    public final StringPath RpPhoto = createString("RpPhoto");

    public QReviewPhotoEntity(String variable) {
        this(ReviewPhotoEntity.class, forVariable(variable), INITS);
    }

    public QReviewPhotoEntity(Path<? extends ReviewPhotoEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewPhotoEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewPhotoEntity(PathMetadata metadata, PathInits inits) {
        this(ReviewPhotoEntity.class, metadata, inits);
    }

    public QReviewPhotoEntity(Class<? extends ReviewPhotoEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReviewEntity(forProperty("review"), inits.get("review")) : null;
    }

}

