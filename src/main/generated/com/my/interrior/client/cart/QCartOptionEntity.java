package com.my.interrior.client.cart;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartOptionEntity is a Querydsl query type for CartOptionEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartOptionEntity extends EntityPathBase<CartOptionEntity> {

    private static final long serialVersionUID = 204780368L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartOptionEntity cartOptionEntity = new QCartOptionEntity("cartOptionEntity");

    public final QCartEntity cartEntity;

    public final NumberPath<Long> CoNo = createNumber("CoNo", Long.class);

    public final com.my.interrior.client.shop.QShopOptionValueEntity shopOptionValueEntity;

    public QCartOptionEntity(String variable) {
        this(CartOptionEntity.class, forVariable(variable), INITS);
    }

    public QCartOptionEntity(Path<? extends CartOptionEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartOptionEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartOptionEntity(PathMetadata metadata, PathInits inits) {
        this(CartOptionEntity.class, metadata, inits);
    }

    public QCartOptionEntity(Class<? extends CartOptionEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cartEntity = inits.isInitialized("cartEntity") ? new QCartEntity(forProperty("cartEntity"), inits.get("cartEntity")) : null;
        this.shopOptionValueEntity = inits.isInitialized("shopOptionValueEntity") ? new com.my.interrior.client.shop.QShopOptionValueEntity(forProperty("shopOptionValueEntity"), inits.get("shopOptionValueEntity")) : null;
    }

}

