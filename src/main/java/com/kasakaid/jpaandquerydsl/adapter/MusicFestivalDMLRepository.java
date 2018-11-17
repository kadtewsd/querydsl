package com.kasakaid.jpaandquerydsl.adapter;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.domain.QMusicFestival;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLUpdateClause;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * QueryDSL の DML は EntityPath を引数に受け受けない。
 * そのため、自分で、RelationalPath に変換する必要があります。
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MusicFestivalDMLRepository {

    private final SQLQueryFactory sqlQueryFactory;

    private RelationalPath<MusicFestival> musicFestivalRelationalPath = new RelationalPathBase<>(
            MusicFestival.class, QMusicFestival.musicFestival.getMetadata(), "", "musicFestival"
    );

    public long updateFestival() {
        SQLUpdateClause updateClause = sqlQueryFactory
                .update(musicFestivalRelationalPath) // relationalPath でないとコンパイルエラー
                .set(QMusicFestival.musicFestival.eventDate, LocalDate.now())
                .where(QMusicFestival.musicFestival.festivalId.eq(1)).addBatch();

        updateClause
                .set(QMusicFestival.musicFestival.place, "川中島")
                .where(QMusicFestival.musicFestival.festivalId.eq(Expressions.asNumber(1))).addBatch();

        return updateClause.execute();

    }

    public long updateFestivalIndividually() {
        long result = sqlQueryFactory
                .update(musicFestivalRelationalPath) // relationalPath でないとコンパイルエラー
                .set(QMusicFestival.musicFestival.eventDate, LocalDate.now())
                .where(QMusicFestival.musicFestival.festivalId.eq(1))
                .execute();

        result += sqlQueryFactory
                .update(musicFestivalRelationalPath)
                .set(QMusicFestival.musicFestival.place, "川中島")
                .where(QMusicFestival.musicFestival.festivalId.eq(Expressions.asNumber(1)))
                .execute();

        return result;

    }
}
