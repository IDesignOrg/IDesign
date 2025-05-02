package com.my.interrior.client.pay;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QShipmentEntity is a Querydsl query type for ShipmentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShipmentEntity extends EntityPathBase<ShipmentEntity> {

    private static final long serialVersionUID = 1306910297L;

    public static final QShipmentEntity shipmentEntity = new QShipmentEntity("shipmentEntity");

    public final StringPath buyerAddr = createString("buyerAddr");

    public final StringPath buyerEmail = createString("buyerEmail");

    public final StringPath buyerName = createString("buyerName");

    public final StringPath buyerPostcode = createString("buyerPostcode");

    public final StringPath buyerTel = createString("buyerTel");

    public final StringPath message = createString("message");

    public final NumberPath<Long> shipmentNo = createNumber("shipmentNo", Long.class);

    public QShipmentEntity(String variable) {
        super(ShipmentEntity.class, forVariable(variable));
    }

    public QShipmentEntity(Path<? extends ShipmentEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QShipmentEntity(PathMetadata metadata) {
        super(ShipmentEntity.class, metadata);
    }

}

