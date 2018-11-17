package com.kasakaid.jpaandquerydsl.adapter;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.domain.QMusicFestival;
import com.kasakaid.jpaandquerydsl.domain.artist.Artist;
import com.kasakaid.jpaandquerydsl.domain.artist.MemberInformation;
import com.kasakaid.jpaandquerydsl.domain.artist.QArtist;
import com.kasakaid.jpaandquerydsl.domain.artist.QMemberInformation;
import com.querydsl.core.types.dsl.EntityPathBase;
import org.springframework.stereotype.Component;

@Component
class EntityDSL {

    QMusicFestival musicFestival = QMusicFestival.musicFestival;
    QMusicFestival mf = new QMusicFestival("mf");
    QArtist artist = QArtist.artist;
    QArtist a = new QArtist("artists");
    // 名前がかぶる場合、テーブル名はmember1 とかいう感じで解釈される。
    QMemberInformation member = QMemberInformation.memberInformation;
    QMemberInformation m = new QMemberInformation("m");

    EntityPathBase<MusicFestival> musicFestival() {
        return this.musicFestival;
    }

    EntityPathBase<MusicFestival> aliasMusicFestival() {
        return this.mf;
    }

    EntityPathBase<Artist> artist() {
        return this.artist;
    }

    EntityPathBase<Artist> aliasArtist() {
        return this.a;
    }

    EntityPathBase<MemberInformation> memberInformation() {
        return this.member;
    }
    EntityPathBase<MemberInformation> aliasMemberInformation() {
        return this.m;
    }
}
