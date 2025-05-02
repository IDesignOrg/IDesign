package com.my.interrior.client.interrior;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInterriorEntity is a Querydsl query type for InterriorEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInterriorEntity extends EntityPathBase<InterriorEntity> {

    private static final long serialVersionUID = 1827777803L;

    public static final QInterriorEntity interriorEntity = new QInterriorEntity("interriorEntity");

    public final StringPath ICategory = createString("ICategory");

    public final StringPath IContent = createString("IContent");

    public final StringPath IImg = createString("IImg");

    public final NumberPath<Long> INo = createNumber("INo", Long.class);

    public final NumberPath<Integer> IPrice = createNumber("IPrice", Integer.class);

    public final NumberPath<Integer> IQuantity = createNumber("IQuantity", Integer.class);

    public final StringPath ITitle = createString("ITitle");

    public final NumberPath<Long> ITotalPrice = createNumber("ITotalPrice", Long.class);

    public QInterriorEntity(String variable) {
        super(InterriorEntity.class, forVariable(variable));
    }

    public QInterriorEntity(Path<? extends InterriorEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInterriorEntity(PathMetadata metadata) {
        super(InterriorEntity.class, metadata);
    }

}

