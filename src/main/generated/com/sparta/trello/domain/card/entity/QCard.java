package com.sparta.trello.domain.card.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCard is a Querydsl query type for Card
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCard extends EntityPathBase<Card> {

    private static final long serialVersionUID = 900606919L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCard card = new QCard("card");

    public final com.sparta.trello.global.entity.QTimestamped _super = new com.sparta.trello.global.entity.QTimestamped(this);

    public final NumberPath<Long> cardId = createNumber("cardId", Long.class);

    public final StringPath cardName = createString("cardName");

    public final StringPath color = createString("color");

    public final com.sparta.trello.domain.column.entity.QColumns column;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.LocalDateTime> dueDate = createDateTime("dueDate", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Integer> sequence = createNumber("sequence", Integer.class);

    public final com.sparta.trello.domain.user.entity.QUser user;

    public final ListPath<Worker, QWorker> workers = this.<Worker, QWorker>createList("workers", Worker.class, QWorker.class, PathInits.DIRECT2);

    public QCard(String variable) {
        this(Card.class, forVariable(variable), INITS);
    }

    public QCard(Path<? extends Card> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCard(PathMetadata metadata, PathInits inits) {
        this(Card.class, metadata, inits);
    }

    public QCard(Class<? extends Card> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.column = inits.isInitialized("column") ? new com.sparta.trello.domain.column.entity.QColumns(forProperty("column"), inits.get("column")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.trello.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

