package com.collabit.project.repository;

import com.collabit.project.domain.entity.TotalScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalScoreRepository extends JpaRepository<TotalScore, Integer> {

}
