package com.collabit.project.repository;

import com.collabit.project.domain.entity.Description;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DescriptionRepository {

    List<Description> findByIdIsPositiveTrue();
}
