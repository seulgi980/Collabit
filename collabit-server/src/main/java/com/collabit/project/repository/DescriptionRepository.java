    package com.collabit.project.repository;

    import com.collabit.project.domain.entity.Description;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.List;

    @Repository
    public interface DescriptionRepository extends JpaRepository<Description, String> {

        List<Description> findByIdIsPositiveTrue();
    }
