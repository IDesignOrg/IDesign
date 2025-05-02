package com.my.interrior.client.cart;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartEntity is a Querydsl query type for CartEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartEntity extends EntityPathBase<CartEntity> {

    private static final long serialVersionUID = -170853253L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartEntity cartEntity = new QCartEntity("cartEntity");

    public final ListPath<CartOptionEntity, QCartOptionEntity> cartOptions = this.<CartOptionEntity, QCartOptionEntity>createList("cartOptions", CartOptionEntity.class, QCartOptionEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> CNo = createNumber("CNo", Long.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final com.my.interrior.client.shop.QShopEntity ShopEntity;

    public final com.my.interrior.client.user.QUserEntity userEntity;

    public QCartEntity(String variable) {
        this(CartEntity.class, forVariable(variable), INITS);
    }

    public QCartEntity(Path<? extends CartEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartEntity(PathMetadata metadata, PathInits inits) {
        this(CartEntity.class, metadata, inits);
    }

    public QCartEntity(Class<? extends CartEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ShopEntity = inits.isInitialized("ShopEntity") ? new com.my.interrior.client.shop.QShopEntity(forProperty("ShopEntity")) : null;
        this.userEntity = inits.isInitialized("userEntity") ? new com.my.interrior.client.user.QUserEntity(forProperty("userEntity")) : null;
    }

}

