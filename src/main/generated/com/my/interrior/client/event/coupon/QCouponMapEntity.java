package com.my.interrior.client.event.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCouponMapEntity is a Querydsl query type for CouponMapEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponMapEntity extends EntityPathBase<CouponMapEntity> {

    private static final long serialVersionUID = -1082653349L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCouponMapEntity couponMapEntity = new QCouponMapEntity("couponMapEntity");

    public final DatePath<java.time.LocalDate> assignedDate = createDate("assignedDate", java.time.LocalDate.class);

    public final QCouponEntity couponEntity;

    public final NumberPath<Long> Id = createNumber("Id", Long.class);

    public final BooleanPath used = createBoolean("used");

    public final DatePath<java.time.LocalDate> usedDate = createDate("usedDate", java.time.LocalDate.class);

    public final com.my.interrior.client.user.QUserEntity userEntity;

    public QCouponMapEntity(String variable) {
        this(CouponMapEntity.class, forVariable(variable), INITS);
    }

    public QCouponMapEntity(Path<? extends CouponMapEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCouponMapEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCouponMapEntity(PathMetadata metadata, PathInits inits) {
        this(CouponMapEntity.class, metadata, inits);
    }

    public QCouponMapEntity(Class<? extends CouponMapEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.couponEntity = inits.isInitialized("couponEntity") ? new QCouponEntity(forProperty("couponEntity")) : null;
        this.userEntity = inits.isInitialized("userEntity") ? new com.my.interrior.client.user.QUserEntity(forProperty("userEntity")) : null;
    }

}

