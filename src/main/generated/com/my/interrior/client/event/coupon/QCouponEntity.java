package com.my.interrior.client.event.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCouponEntity is a Querydsl query type for CouponEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponEntity extends EntityPathBase<CouponEntity> {

    private static final long serialVersionUID = -378380185L;

    public static final QCouponEntity couponEntity = new QCouponEntity("couponEntity");

    public final DatePath<java.time.LocalDate> couponCreateAt = createDate("couponCreateAt", java.time.LocalDate.class);

    public final NumberPath<Integer> couponDiscount = createNumber("couponDiscount", Integer.class);

    public final DatePath<java.time.LocalDate> couponEndAt = createDate("couponEndAt", java.time.LocalDate.class);

    public final NumberPath<Integer> couponLimit = createNumber("couponLimit", Integer.class);

    public final StringPath couponName = createString("couponName");

    public final NumberPath<Long> couponNo = createNumber("couponNo", Long.class);

    public final DatePath<java.time.LocalDate> couponStartAt = createDate("couponStartAt", java.time.LocalDate.class);

    public final StringPath couponState = createString("couponState");

    public final NumberPath<Double> couponValue = createNumber("couponValue", Double.class);

    public QCouponEntity(String variable) {
        super(CouponEntity.class, forVariable(variable));
    }

    public QCouponEntity(Path<? extends CouponEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCouponEntity(PathMetadata metadata) {
        super(CouponEntity.class, metadata);
    }

}

