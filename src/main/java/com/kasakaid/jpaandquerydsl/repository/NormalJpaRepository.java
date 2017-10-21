package com.kasakaid.jpaandquerydsl.repository;

import com.kasakaid.jpaandquerydsl.domain.MusicFestival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NormalJpaRepository extends JpaRepository<MusicFestival, Integer> {
}
