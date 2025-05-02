package com.my.interrior.client.pay;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPayEntity is a Querydsl query type for PayEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPayEntity extends EntityPathBase<PayEntity> {

    private static final long serialVersionUID = 1591309551L;

    public static final QPayEntity payEntity = new QPayEntity("payEntity");

    public final StringPath cardName = createString("cardName");

    public final StringPath merchantUId = createString("merchantUId");

    public final StringPath name = createString("name");

    public final NumberPath<java.math.BigDecimal> paidAmount = createNumber("paidAmount", java.math.BigDecimal.class);

    public final DatePath<java.time.LocalDate> paidAt = createDate("paidAt", java.time.LocalDate.class);

    public final StringPath payMethod = createString("payMethod");

    public final NumberPath<Long> payNo = createNumber("payNo", Long.class);

    public final StringPath status = createString("status");

    public final BooleanPath success = createBoolean("success");

    public QPayEntity(String variable) {
        super(PayEntity.class, forVariable(variable));
    }

    public QPayEntity(Path<? extends PayEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPayEntity(PathMetadata metadata) {
        super(PayEntity.class, metadata);
    }

}

