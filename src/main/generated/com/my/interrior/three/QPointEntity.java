package com.my.interrior.three;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPointEntity is a Querydsl query type for PointEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPointEntity extends EntityPathBase<PointEntity> {

    private static final long serialVersionUID = 1112814390L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPointEntity pointEntity = new QPointEntity("pointEntity");

    public final QDataEntity data;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> x = createNumber("x", Double.class);

    public final NumberPath<Double> y = createNumber("y", Double.class);

    public final NumberPath<Double> z = createNumber("z", Double.class);

    public QPointEntity(String variable) {
        this(PointEntity.class, forVariable(variable), INITS);
    }

    public QPointEntity(Path<? extends PointEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPointEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPointEntity(PathMetadata metadata, PathInits inits) {
        this(PointEntity.class, metadata, inits);
    }

    public QPointEntity(Class<? extends PointEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.data = inits.isInitialized("data") ? new QDataEntity(forProperty("data"), inits.get("data")) : null;
    }

}

