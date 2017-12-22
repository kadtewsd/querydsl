package com.kasakaid.jpaandquerydsl.service;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import com.kasakaid.jpaandquerydsl.domain.artist.MemberInformation;
import com.kasakaid.jpaandquerydsl.repository.JpaMusicFestivalRepository;
import com.kasakaid.jpaandquerydsl.repository.MusicFestivalRepository;
import com.kasakaid.jpaandquerydsl.repository.NormalJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@RequiredArgsConstructor
//@Transactional("transactionManager")
@Transactional
public class MusicFestivalService {

    private final MusicFestivalRepository musicFestivalRepository;
    private final NormalJpaRepository normalJpaRepository;
    private final JpaMusicFestivalRepository jpaMusicFestivalRepository;
    public List<MusicFestival> findMusicFestival() {
        return musicFestivalRepository.findMusicFestival();
    }

    public <T extends Serializable> List<MusicFestival> findMusicFestivalWhereJoin() {
        return musicFestivalRepository.findMusicFestivalWhereJoin();
    }
    public List<MusicFestival> findMusicFestivalByTransform() {
        return musicFestivalRepository.findMusicFestivalByTransform();
    }

    public List<MusicFestival> findMusicFestivalByList() {
        return musicFestivalRepository.findMusicFestivalByList();
    }

    public List<MusicFestival> findMusicFestivalByReverse() {
        return musicFestivalRepository.findMusicFestivalByReverse();
    }

    public List<MusicFestival> findMusicFestivalByNestedList() {
        return musicFestivalRepository.findMusicFestivalByNestedList();
    }

    public List<MemberInformation> findMusicFestivalByBottom() {
        return musicFestivalRepository.findMusicFestivalByBottom();
    }
    public List<MusicFestival> findMusicFestivalByJPAQuery() {
        return jpaMusicFestivalRepository.findMusicFestivalByJPAQuery();
    }
    public List<MusicFestival> findAll() {
        return normalJpaRepository.findAll();
    }
}
