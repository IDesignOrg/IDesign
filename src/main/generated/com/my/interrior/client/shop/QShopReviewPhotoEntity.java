package com.my.interrior.client.shop;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShopReviewPhotoEntity is a Querydsl query type for ShopReviewPhotoEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShopReviewPhotoEntity extends EntityPathBase<ShopReviewPhotoEntity> {

    private static final long serialVersionUID = 2099520741L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShopReviewPhotoEntity shopReviewPhotoEntity = new QShopReviewPhotoEntity("shopReviewPhotoEntity");

    public final QShopReviewEntity shopReviewEntity;

    public final NumberPath<Long> shopReviewPhotoNo = createNumber("shopReviewPhotoNo", Long.class);

    public final StringPath shopReviewPhotoUrl = createString("shopReviewPhotoUrl");

    public QShopReviewPhotoEntity(String variable) {
        this(ShopReviewPhotoEntity.class, forVariable(variable), INITS);
    }

    public QShopReviewPhotoEntity(Path<? extends ShopReviewPhotoEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShopReviewPhotoEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShopReviewPhotoEntity(PathMetadata metadata, PathInits inits) {
        this(ShopReviewPhotoEntity.class, metadata, inits);
    }

    public QShopReviewPhotoEntity(Class<? extends ShopReviewPhotoEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.shopReviewEntity = inits.isInitialized("shopReviewEntity") ? new QShopReviewEntity(forProperty("shopReviewEntity"), inits.get("shopReviewEntity")) : null;
    }

}

