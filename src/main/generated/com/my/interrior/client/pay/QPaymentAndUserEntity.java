package com.my.interrior.client.pay;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPaymentAndUserEntity is a Querydsl query type for PaymentAndUserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentAndUserEntity extends EntityPathBase<PaymentAndUserEntity> {

    private static final long serialVersionUID = 288767899L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPaymentAndUserEntity paymentAndUserEntity = new QPaymentAndUserEntity("paymentAndUserEntity");

    public final QPayEntity payEntity;

    public final NumberPath<Long> paymentUserNo = createNumber("paymentUserNo", Long.class);

    public final com.my.interrior.client.user.QUserEntity userEntity;

    public QPaymentAndUserEntity(String variable) {
        this(PaymentAndUserEntity.class, forVariable(variable), INITS);
    }

    public QPaymentAndUserEntity(Path<? extends PaymentAndUserEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPaymentAndUserEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPaymentAndUserEntity(PathMetadata metadata, PathInits inits) {
        this(PaymentAndUserEntity.class, metadata, inits);
    }

    public QPaymentAndUserEntity(Class<? extends PaymentAndUserEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.payEntity = inits.isInitialized("payEntity") ? new QPayEntity(forProperty("payEntity")) : null;
        this.userEntity = inits.isInitialized("userEntity") ? new com.my.interrior.client.user.QUserEntity(forProperty("userEntity")) : null;
    }

}

