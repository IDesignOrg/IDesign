package com.my.interrior.three;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDataEntity is a Querydsl query type for DataEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDataEntity extends EntityPathBase<DataEntity> {

    private static final long serialVersionUID = 218701354L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDataEntity dataEntity = new QDataEntity("dataEntity");

    public final ListPath<String, StringPath> children = this.<String, StringPath>createList("children", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath oid = createString("oid");

    public final StringPath parent = createString("parent");

    public final ListPath<PointEntity, QPointEntity> points = this.<PointEntity, QPointEntity>createList("points", PointEntity.class, QPointEntity.class, PathInits.DIRECT2);

    public final NumberPath<Double> rotation = createNumber("rotation", Double.class);

    public final QThreeEntity threeEntity;

    public final StringPath type = createString("type");

    public QDataEntity(String variable) {
        this(DataEntity.class, forVariable(variable), INITS);
    }

    public QDataEntity(Path<? extends DataEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDataEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDataEntity(PathMetadata metadata, PathInits inits) {
        this(DataEntity.class, metadata, inits);
    }

    public QDataEntity(Class<? extends DataEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.threeEntity = inits.isInitialized("threeEntity") ? new QThreeEntity(forProperty("threeEntity"), inits.get("threeEntity")) : null;
    }

}

