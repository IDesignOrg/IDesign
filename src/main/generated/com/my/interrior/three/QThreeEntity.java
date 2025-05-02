package com.my.interrior.three;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QThreeEntity is a Querydsl query type for ThreeEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QThreeEntity extends EntityPathBase<ThreeEntity> {

    private static final long serialVersionUID = 1575272644L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QThreeEntity threeEntity = new QThreeEntity("threeEntity");

    public final ListPath<DataEntity, QDataEntity> dataEntity = this.<DataEntity, QDataEntity>createList("dataEntity", DataEntity.class, QDataEntity.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> modDate = createDateTime("modDate", java.time.LocalDateTime.class);

    public final StringPath projectId = createString("projectId");

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final StringPath src = createString("src");

    public final StringPath thumbnail = createString("thumbnail");

    public final StringPath title = createString("title");

    public final com.my.interrior.client.user.QUserEntity userEntity;

    public QThreeEntity(String variable) {
        this(ThreeEntity.class, forVariable(variable), INITS);
    }

    public QThreeEntity(Path<? extends ThreeEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QThreeEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QThreeEntity(PathMetadata metadata, PathInits inits) {
        this(ThreeEntity.class, metadata, inits);
    }

    public QThreeEntity(Class<? extends ThreeEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userEntity = inits.isInitialized("userEntity") ? new com.my.interrior.client.user.QUserEntity(forProperty("userEntity")) : null;
    }

}

