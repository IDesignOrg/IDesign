package com.my.interrior.client.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = -427161765L;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final ListPath<com.my.interrior.client.csc.faq.FaqEntity, com.my.interrior.client.csc.faq.QFaqEntity> faq = this.<com.my.interrior.client.csc.faq.FaqEntity, com.my.interrior.client.csc.faq.QFaqEntity>createList("faq", com.my.interrior.client.csc.faq.FaqEntity.class, com.my.interrior.client.csc.faq.QFaqEntity.class, PathInits.DIRECT2);

    public final ListPath<com.my.interrior.client.csc.inquiry.InquiryEntity, com.my.interrior.client.csc.inquiry.QInquiryEntity> Inquiries = this.<com.my.interrior.client.csc.inquiry.InquiryEntity, com.my.interrior.client.csc.inquiry.QInquiryEntity>createList("Inquiries", com.my.interrior.client.csc.inquiry.InquiryEntity.class, com.my.interrior.client.csc.inquiry.QInquiryEntity.class, PathInits.DIRECT2);

    public final ListPath<com.my.interrior.client.csc.notice.NoticeEntity, com.my.interrior.client.csc.notice.QNoticeEntity> notices = this.<com.my.interrior.client.csc.notice.NoticeEntity, com.my.interrior.client.csc.notice.QNoticeEntity>createList("notices", com.my.interrior.client.csc.notice.NoticeEntity.class, com.my.interrior.client.csc.notice.QNoticeEntity.class, PathInits.DIRECT2);

    public final StringPath UBirth = createString("UBirth");

    public final BooleanPath UDeactivated = createBoolean("UDeactivated");

    public final StringPath UId = createString("UId");

    public final StringPath UMail = createString("UMail");

    public final StringPath UName = createString("UName");

    public final NumberPath<Long> UNo = createNumber("UNo", Long.class);

    public final StringPath UPofile = createString("UPofile");

    public final StringPath UPw = createString("UPw");

    public final DatePath<java.time.LocalDate> URegister = createDate("URegister", java.time.LocalDate.class);

    public final StringPath UTel = createString("UTel");

    public QUserEntity(String variable) {
        super(UserEntity.class, forVariable(variable));
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEntity(PathMetadata metadata) {
        super(UserEntity.class, metadata);
    }

}

