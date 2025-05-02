package com.my.interrior.three;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChildRelationshipEntity is a Querydsl query type for ChildRelationshipEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChildRelationshipEntity extends EntityPathBase<ChildRelationshipEntity> {

    private static final long serialVersionUID = -1724908646L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChildRelationshipEntity childRelationshipEntity = new QChildRelationshipEntity("childRelationshipEntity");

    public final QDataEntity child;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QDataEntity parent;

    public QChildRelationshipEntity(String variable) {
        this(ChildRelationshipEntity.class, forVariable(variable), INITS);
    }

    public QChildRelationshipEntity(Path<? extends ChildRelationshipEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChildRelationshipEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChildRelationshipEntity(PathMetadata metadata, PathInits inits) {
        this(ChildRelationshipEntity.class, metadata, inits);
    }

    public QChildRelationshipEntity(Class<? extends ChildRelationshipEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.child = inits.isInitialized("child") ? new QDataEntity(forProperty("child"), inits.get("child")) : null;
        this.parent = inits.isInitialized("parent") ? new QDataEntity(forProperty("parent"), inits.get("parent")) : null;
    }

}

