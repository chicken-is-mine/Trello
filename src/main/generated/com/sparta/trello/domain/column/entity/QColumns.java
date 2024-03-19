package com.sparta.trello.domain.column.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QColumns is a Querydsl query type for Columns
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QColumns extends EntityPathBase<Columns> {

    private static final long serialVersionUID = -197188288L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QColumns columns = new QColumns("columns");

    public final com.sparta.trello.domain.board.entity.QBoard board;

    public final NumberPath<Long> columnId = createNumber("columnId", Long.class);

    public final StringPath columnName = createString("columnName");

    public final NumberPath<Integer> sequence = createNumber("sequence", Integer.class);

    public QColumns(String variable) {
        this(Columns.class, forVariable(variable), INITS);
    }

    public QColumns(Path<? extends Columns> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QColumns(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QColumns(PathMetadata metadata, PathInits inits) {
        this(Columns.class, metadata, inits);
    }

    public QColumns(Class<? extends Columns> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.sparta.trello.domain.board.entity.QBoard(forProperty("board"), inits.get("board")) : null;
    }

}

