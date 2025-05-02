package com.my.interrior.client.shop;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShopReviewUserMapping is a Querydsl query type for ShopReviewUserMapping
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShopReviewUserMapping extends EntityPathBase<ShopReviewUserMapping> {

    private static final long serialVersionUID = 1940061875L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShopReviewUserMapping shopReviewUserMapping = new QShopReviewUserMapping("shopReviewUserMapping");

    public final QShopEntity shopEntity;

    public final QShopReviewEntity shopReivewNoEntity;

    public final NumberPath<Long> shopReivewUserNo = createNumber("shopReivewUserNo", Long.class);

    public final com.my.interrior.client.user.QUserEntity userEntity;

    public QShopReviewUserMapping(String variable) {
        this(ShopReviewUserMapping.class, forVariable(variable), INITS);
    }

    public QShopReviewUserMapping(Path<? extends ShopReviewUserMapping> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShopReviewUserMapping(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShopReviewUserMapping(PathMetadata metadata, PathInits inits) {
        this(ShopReviewUserMapping.class, metadata, inits);
    }

    public QShopReviewUserMapping(Class<? extends ShopReviewUserMapping> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.shopEntity = inits.isInitialized("shopEntity") ? new QShopEntity(forProperty("shopEntity")) : null;
        this.shopReivewNoEntity = inits.isInitialized("shopReivewNoEntity") ? new QShopReviewEntity(forProperty("shopReivewNoEntity"), inits.get("shopReivewNoEntity")) : null;
        this.userEntity = inits.isInitialized("userEntity") ? new com.my.interrior.client.user.QUserEntity(forProperty("userEntity")) : null;
    }

}

