package com.sabc.digitalchampions.repository;

import com.sabc.digitalchampions.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByTitle(String title);

    @Query("select c from Course c where c.title like %:key%")
    Page<Course> findAll(@Param("key") String key, Pageable pageable);

    Course findByRef(String ref);

    Page<Course> findAllByPublished(boolean published, Pageable pageable);

    boolean existsByRef(String ref);
}
