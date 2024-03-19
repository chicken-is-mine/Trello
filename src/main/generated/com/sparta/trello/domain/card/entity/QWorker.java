package com.sparta.trello.domain.card.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorker is a Querydsl query type for Worker
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorker extends EntityPathBase<Worker> {

    private static final long serialVersionUID = -1514622347L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorker worker = new QWorker("worker");

    public final QCard card;

    public final com.sparta.trello.domain.user.entity.QUser user;

    public final NumberPath<Long> workerId = createNumber("workerId", Long.class);

    public QWorker(String variable) {
        this(Worker.class, forVariable(variable), INITS);
    }

    public QWorker(Path<? extends Worker> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorker(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorker(PathMetadata metadata, PathInits inits) {
        this(Worker.class, metadata, inits);
    }

    public QWorker(Class<? extends Worker> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.card = inits.isInitialized("card") ? new QCard(forProperty("card"), inits.get("card")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.trello.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

