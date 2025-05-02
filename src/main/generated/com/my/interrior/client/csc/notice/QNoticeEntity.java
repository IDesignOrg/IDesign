package com.my.interrior.client.csc.notice;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNoticeEntity is a Querydsl query type for NoticeEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNoticeEntity extends EntityPathBase<NoticeEntity> {

    private static final long serialVersionUID = -1554698368L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNoticeEntity noticeEntity = new QNoticeEntity("noticeEntity");

    public final StringPath notAuthor = createString("notAuthor");

    public final StringPath notContent = createString("notContent");

    public final NumberPath<Long> notNo = createNumber("notNo", Long.class);

    public final DatePath<java.time.LocalDate> notRegisteredDate = createDate("notRegisteredDate", java.time.LocalDate.class);

    public final StringPath notTitle = createString("notTitle");

    public final com.my.interrior.client.user.QUserEntity userEntity;

    public QNoticeEntity(String variable) {
        this(NoticeEntity.class, forVariable(variable), INITS);
    }

    public QNoticeEntity(Path<? extends NoticeEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNoticeEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNoticeEntity(PathMetadata metadata, PathInits inits) {
        this(NoticeEntity.class, metadata, inits);
    }

    public QNoticeEntity(Class<? extends NoticeEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userEntity = inits.isInitialized("userEntity") ? new com.my.interrior.client.user.QUserEntity(forProperty("userEntity")) : null;
    }

}

