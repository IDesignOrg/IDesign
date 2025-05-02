package com.my.interrior.client.shop;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShopOptionValueEntity is a Querydsl query type for ShopOptionValueEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShopOptionValueEntity extends EntityPathBase<ShopOptionValueEntity> {

    private static final long serialVersionUID = -33335385L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShopOptionValueEntity shopOptionValueEntity = new QShopOptionValueEntity("shopOptionValueEntity");

    public final QShopOptionEntity shopOptionEntity;

    public final NumberPath<Integer> shopOptionPrice = createNumber("shopOptionPrice", Integer.class);

    public final StringPath shopOptionValue = createString("shopOptionValue");

    public final NumberPath<Long> shopOptionValueNo = createNumber("shopOptionValueNo", Long.class);

    public QShopOptionValueEntity(String variable) {
        this(ShopOptionValueEntity.class, forVariable(variable), INITS);
    }

    public QShopOptionValueEntity(Path<? extends ShopOptionValueEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShopOptionValueEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShopOptionValueEntity(PathMetadata metadata, PathInits inits) {
        this(ShopOptionValueEntity.class, metadata, inits);
    }

    public QShopOptionValueEntity(Class<? extends ShopOptionValueEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.shopOptionEntity = inits.isInitialized("shopOptionEntity") ? new QShopOptionEntity(forProperty("shopOptionEntity"), inits.get("shopOptionEntity")) : null;
    }

}

