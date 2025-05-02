package com.my.interrior.client.csc.inquiry;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInquiryAnswerEntity is a Querydsl query type for InquiryAnswerEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInquiryAnswerEntity extends EntityPathBase<InquiryAnswerEntity> {

    private static final long serialVersionUID = -2045688304L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInquiryAnswerEntity inquiryAnswerEntity = new QInquiryAnswerEntity("inquiryAnswerEntity");

    public final StringPath ansContent = createString("ansContent");

    public final NumberPath<Long> ansNo = createNumber("ansNo", Long.class);

    public final DatePath<java.time.LocalDate> ansRegisteredDate = createDate("ansRegisteredDate", java.time.LocalDate.class);

    public final QInquiryEntity inquiry;

    public final com.my.interrior.client.user.QUserEntity userEntity;

    public QInquiryAnswerEntity(String variable) {
        this(InquiryAnswerEntity.class, forVariable(variable), INITS);
    }

    public QInquiryAnswerEntity(Path<? extends InquiryAnswerEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInquiryAnswerEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInquiryAnswerEntity(PathMetadata metadata, PathInits inits) {
        this(InquiryAnswerEntity.class, metadata, inits);
    }

    public QInquiryAnswerEntity(Class<? extends InquiryAnswerEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.inquiry = inits.isInitialized("inquiry") ? new QInquiryEntity(forProperty("inquiry"), inits.get("inquiry")) : null;
        this.userEntity = inits.isInitialized("userEntity") ? new com.my.interrior.client.user.QUserEntity(forProperty("userEntity")) : null;
    }

}

