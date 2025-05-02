package com.my.interrior.client.shop;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShopOptionEntity is a Querydsl query type for ShopOptionEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShopOptionEntity extends EntityPathBase<ShopOptionEntity> {

    private static final long serialVersionUID = -563967728L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShopOptionEntity shopOptionEntity = new QShopOptionEntity("shopOptionEntity");

    public final QShopEntity shopEntity;

    public final StringPath shopOptionName = createString("shopOptionName");

    public final NumberPath<Long> shopOptionNo = createNumber("shopOptionNo", Long.class);

    public final ListPath<ShopOptionValueEntity, QShopOptionValueEntity> shopOptionValues = this.<ShopOptionValueEntity, QShopOptionValueEntity>createList("shopOptionValues", ShopOptionValueEntity.class, QShopOptionValueEntity.class, PathInits.DIRECT2);

    public QShopOptionEntity(String variable) {
        this(ShopOptionEntity.class, forVariable(variable), INITS);
    }

    public QShopOptionEntity(Path<? extends ShopOptionEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShopOptionEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShopOptionEntity(PathMetadata metadata, PathInits inits) {
        this(ShopOptionEntity.class, metadata, inits);
    }

    public QShopOptionEntity(Class<? extends ShopOptionEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.shopEntity = inits.isInitialized("shopEntity") ? new QShopEntity(forProperty("shopEntity")) : null;
    }

}

