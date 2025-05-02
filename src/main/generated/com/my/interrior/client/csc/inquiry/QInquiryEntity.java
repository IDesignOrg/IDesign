package com.my.interrior.client.csc.inquiry;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInquiryEntity is a Querydsl query type for InquiryEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInquiryEntity extends EntityPathBase<InquiryEntity> {

    private static final long serialVersionUID = 37939186L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInquiryEntity inquiryEntity = new QInquiryEntity("inquiryEntity");

    public final StringPath inqCategory = createString("inqCategory");

    public final StringPath inqContent = createString("inqContent");

    public final NumberPath<Long> inqNo = createNumber("inqNo", Long.class);

    public final DatePath<java.time.LocalDate> inqRegisteredDate = createDate("inqRegisteredDate", java.time.LocalDate.class);

    public final StringPath inqTitle = createString("inqTitle");

    public final com.my.interrior.client.user.QUserEntity userEntity;

    public QInquiryEntity(String variable) {
        this(InquiryEntity.class, forVariable(variable), INITS);
    }

    public QInquiryEntity(Path<? extends InquiryEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInquiryEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInquiryEntity(PathMetadata metadata, PathInits inits) {
        this(InquiryEntity.class, metadata, inits);
    }

    public QInquiryEntity(Class<? extends InquiryEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userEntity = inits.isInitialized("userEntity") ? new com.my.interrior.client.user.QUserEntity(forProperty("userEntity")) : null;
    }

}

