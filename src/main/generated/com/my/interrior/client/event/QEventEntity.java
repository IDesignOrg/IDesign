package com.my.interrior.client.event;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEventEntity is a Querydsl query type for EventEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventEntity extends EntityPathBase<EventEntity> {

    private static final long serialVersionUID = 190880979L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEventEntity eventEntity = new QEventEntity("eventEntity");

    public final com.my.interrior.client.event.coupon.QCouponEntity coupon;

    public final StringPath eventContent = createString("eventContent");

    public final StringPath eventImg = createString("eventImg");

    public final NumberPath<Long> eventNo = createNumber("eventNo", Long.class);

    public final StringPath eventTitle = createString("eventTitle");

    public QEventEntity(String variable) {
        this(EventEntity.class, forVariable(variable), INITS);
    }

    public QEventEntity(Path<? extends EventEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEventEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEventEntity(PathMetadata metadata, PathInits inits) {
        this(EventEntity.class, metadata, inits);
    }

    public QEventEntity(Class<? extends EventEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coupon = inits.isInitialized("coupon") ? new com.my.interrior.client.event.coupon.QCouponEntity(forProperty("coupon")) : null;
    }

}

