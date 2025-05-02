package com.my.interrior.client.csc.recover;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecoveryEntity is a Querydsl query type for RecoveryEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecoveryEntity extends EntityPathBase<RecoveryEntity> {

    private static final long serialVersionUID = -250329037L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecoveryEntity recoveryEntity = new QRecoveryEntity("recoveryEntity");

    public final DatePath<java.time.LocalDate> processedDate = createDate("processedDate", java.time.LocalDate.class);

    public final StringPath reason = createString("reason");

    public final NumberPath<Long> RecoverNo = createNumber("RecoverNo", Long.class);

    public final DatePath<java.time.LocalDate> requestDate = createDate("requestDate", java.time.LocalDate.class);

    public final StringPath status = createString("status");

    public final com.my.interrior.client.user.QUserEntity user;

    public QRecoveryEntity(String variable) {
        this(RecoveryEntity.class, forVariable(variable), INITS);
    }

    public QRecoveryEntity(Path<? extends RecoveryEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecoveryEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecoveryEntity(PathMetadata metadata, PathInits inits) {
        this(RecoveryEntity.class, metadata, inits);
    }

    public QRecoveryEntity(Class<? extends RecoveryEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.my.interrior.client.user.QUserEntity(forProperty("user")) : null;
    }

}

