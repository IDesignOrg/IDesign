package com.my.interrior.client.ordered;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderedEntity is a Querydsl query type for OrderedEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderedEntity extends EntityPathBase<OrderedEntity> {

    private static final long serialVersionUID = 307919097L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderedEntity orderedEntity = new QOrderedEntity("orderedEntity");

    public final StringPath merchantUId = createString("merchantUId");

    public final DatePath<java.time.LocalDate> orderedDate = createDate("orderedDate", java.time.LocalDate.class);

    public final NumberPath<Long> orderedNo = createNumber("orderedNo", Long.class);

    public final NumberPath<Long> orderedNumber = createNumber("orderedNumber", Long.class);

    public final StringPath orderedState = createString("orderedState");

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final StringPath shipmentState = createString("shipmentState");

    public final NumberPath<Long> shopNo = createNumber("shopNo", Long.class);

    public final com.my.interrior.client.user.QUserEntity userEntity;

    public QOrderedEntity(String variable) {
        this(OrderedEntity.class, forVariable(variable), INITS);
    }

    public QOrderedEntity(Path<? extends OrderedEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderedEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderedEntity(PathMetadata metadata, PathInits inits) {
        this(OrderedEntity.class, metadata, inits);
    }

    public QOrderedEntity(Class<? extends OrderedEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userEntity = inits.isInitialized("userEntity") ? new com.my.interrior.client.user.QUserEntity(forProperty("userEntity")) : null;
    }

}

