package com.my.interrior.client.csc.faq;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFaqEntity is a Querydsl query type for FaqEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFaqEntity extends EntityPathBase<FaqEntity> {

    private static final long serialVersionUID = 547561488L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFaqEntity faqEntity = new QFaqEntity("faqEntity");

    public final StringPath faqAuthor = createString("faqAuthor");

    public final StringPath faqCategory = createString("faqCategory");

    public final StringPath faqContent = createString("faqContent");

    public final NumberPath<Long> faqNo = createNumber("faqNo", Long.class);

    public final DatePath<java.time.LocalDate> faqRegisteredDate = createDate("faqRegisteredDate", java.time.LocalDate.class);

    public final StringPath faqTitle = createString("faqTitle");

    public final com.my.interrior.client.user.QUserEntity userEntity;

    public QFaqEntity(String variable) {
        this(FaqEntity.class, forVariable(variable), INITS);
    }

    public QFaqEntity(Path<? extends FaqEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFaqEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFaqEntity(PathMetadata metadata, PathInits inits) {
        this(FaqEntity.class, metadata, inits);
    }

    public QFaqEntity(Class<? extends FaqEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userEntity = inits.isInitialized("userEntity") ? new com.my.interrior.client.user.QUserEntity(forProperty("userEntity")) : null;
    }

}

