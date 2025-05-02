package com.my.interrior.client.pay;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPrePaymentEntity is a Querydsl query type for PrePaymentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPrePaymentEntity extends EntityPathBase<PrePaymentEntity> {

    private static final long serialVersionUID = 598908130L;

    public static final QPrePaymentEntity prePaymentEntity = new QPrePaymentEntity("prePaymentEntity");

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final StringPath merchant_uid = createString("merchant_uid");

    public final NumberPath<Long> prePaymentId = createNumber("prePaymentId", Long.class);

    public QPrePaymentEntity(String variable) {
        super(PrePaymentEntity.class, forVariable(variable));
    }

    public QPrePaymentEntity(Path<? extends PrePaymentEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPrePaymentEntity(PathMetadata metadata) {
        super(PrePaymentEntity.class, metadata);
    }

}

