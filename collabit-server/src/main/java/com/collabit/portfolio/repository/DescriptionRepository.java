package com.collabit.portfolio.repository;

import com.collabit.portfolio.domain.DescriptionId;
import com.collabit.portfolio.domain.entity.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, DescriptionId> {
    Optional<Description> findById_CodeAndId_IsPositive(String code, Boolean isPositive);

    @Query("SELECT DISTINCT d.name FROM Description d")
    List<String> findDistinctNames();

    Description findByNameAndId_IsPositive(String name, Boolean isPositive);
}
