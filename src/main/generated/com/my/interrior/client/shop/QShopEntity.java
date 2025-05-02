package com.my.interrior.client.shop;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QShopEntity is a Querydsl query type for ShopEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShopEntity extends EntityPathBase<ShopEntity> {

    private static final long serialVersionUID = 495067707L;

    public static final QShopEntity shopEntity = new QShopEntity("shopEntity");

    public final BooleanPath SDeactivated = createBoolean("SDeactivated");

    public final StringPath shopCategory = createString("shopCategory");

    public final StringPath shopContent = createString("shopContent");

    public final StringPath shopDiscont = createString("shopDiscont");

    public final NumberPath<Integer> shopHit = createNumber("shopHit", Integer.class);

    public final StringPath shopMainPhoto = createString("shopMainPhoto");

    public final NumberPath<Long> shopNo = createNumber("shopNo", Long.class);

    public final StringPath shopPrice = createString("shopPrice");

    public final NumberPath<Integer> shopRefundCount = createNumber("shopRefundCount", Integer.class);

    public final NumberPath<Integer> shopSell = createNumber("shopSell", Integer.class);

    public final StringPath shopTitle = createString("shopTitle");

    public final DateTimePath<java.time.LocalDateTime> ShopWriteTime = createDateTime("ShopWriteTime", java.time.LocalDateTime.class);

    public QShopEntity(String variable) {
        super(ShopEntity.class, forVariable(variable));
    }

    public QShopEntity(Path<? extends ShopEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QShopEntity(PathMetadata metadata) {
        super(ShopEntity.class, metadata);
    }

}

