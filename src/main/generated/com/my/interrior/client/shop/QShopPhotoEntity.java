package com.my.interrior.client.shop;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShopPhotoEntity is a Querydsl query type for ShopPhotoEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShopPhotoEntity extends EntityPathBase<ShopPhotoEntity> {

    private static final long serialVersionUID = 1336678749L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShopPhotoEntity shopPhotoEntity = new QShopPhotoEntity("shopPhotoEntity");

    public final QShopEntity shopEntity;

    public final NumberPath<Long> shopPhotoNo = createNumber("shopPhotoNo", Long.class);

    public final StringPath shopPhotoUrl = createString("shopPhotoUrl");

    public QShopPhotoEntity(String variable) {
        this(ShopPhotoEntity.class, forVariable(variable), INITS);
    }

    public QShopPhotoEntity(Path<? extends ShopPhotoEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShopPhotoEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShopPhotoEntity(PathMetadata metadata, PathInits inits) {
        this(ShopPhotoEntity.class, metadata, inits);
    }

    public QShopPhotoEntity(Class<? extends ShopPhotoEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.shopEntity = inits.isInitialized("shopEntity") ? new QShopEntity(forProperty("shopEntity")) : null;
    }

}

