package com.my.interrior.client.ordered;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderedRefundEntity is a Querydsl query type for OrderedRefundEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderedRefundEntity extends EntityPathBase<OrderedRefundEntity> {

    private static final long serialVersionUID = 999629329L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderedRefundEntity orderedRefundEntity = new QOrderedRefundEntity("orderedRefundEntity");

    public final QOrderedEntity orderedEntity;

    public final NumberPath<Long> refundNo = createNumber("refundNo", Long.class);

    public final StringPath refundReason = createString("refundReason");

    public final StringPath refundUser = createString("refundUser");

    public QOrderedRefundEntity(String variable) {
        this(OrderedRefundEntity.class, forVariable(variable), INITS);
    }

    public QOrderedRefundEntity(Path<? extends OrderedRefundEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderedRefundEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderedRefundEntity(PathMetadata metadata, PathInits inits) {
        this(OrderedRefundEntity.class, metadata, inits);
    }

    public QOrderedRefundEntity(Class<? extends OrderedRefundEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.orderedEntity = inits.isInitialized("orderedEntity") ? new QOrderedEntity(forProperty("orderedEntity"), inits.get("orderedEntity")) : null;
    }

}

