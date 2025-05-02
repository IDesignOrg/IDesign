package com.my.interrior.client.shop;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShopReviewEntity is a Querydsl query type for ShopReviewEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShopReviewEntity extends EntityPathBase<ShopReviewEntity> {

    private static final long serialVersionUID = 1166213555L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShopReviewEntity shopReviewEntity = new QShopReviewEntity("shopReviewEntity");

    public final QShopEntity shopEntity;

    public final StringPath shopReviewContent = createString("shopReviewContent");

    public final DateTimePath<java.time.LocalDateTime> shopReviewCreated = createDateTime("shopReviewCreated", java.time.LocalDateTime.class);

    public final NumberPath<Long> shopReviewNo = createNumber("shopReviewNo", Long.class);

    public final NumberPath<Double> shopReviewStarRating = createNumber("shopReviewStarRating", Double.class);

    public final com.my.interrior.client.user.QUserEntity user;

    public QShopReviewEntity(String variable) {
        this(ShopReviewEntity.class, forVariable(variable), INITS);
    }

    public QShopReviewEntity(Path<? extends ShopReviewEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShopReviewEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShopReviewEntity(PathMetadata metadata, PathInits inits) {
        this(ShopReviewEntity.class, metadata, inits);
    }

    public QShopReviewEntity(Class<? extends ShopReviewEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.shopEntity = inits.isInitialized("shopEntity") ? new QShopEntity(forProperty("shopEntity")) : null;
        this.user = inits.isInitialized("user") ? new com.my.interrior.client.user.QUserEntity(forProperty("user")) : null;
    }

}

