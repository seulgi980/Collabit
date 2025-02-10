package com.collabit.portfolio.repository;

import com.collabit.portfolio.domain.entity.Description;
import com.collabit.portfolio.repository.projection.DescriptionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, String> {

    List<DescriptionProjection> findAllProjectedBy();
}
