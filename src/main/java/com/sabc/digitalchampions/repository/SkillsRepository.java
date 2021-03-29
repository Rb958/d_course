package com.sabc.digitalchampions.repository;

import com.sabc.digitalchampions.entity.Course;
import com.sabc.digitalchampions.entity.Skills;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SkillsRepository extends JpaRepository<Skills, Integer> {
    @Query("select s from Skills s where s.label like %?1%")
    Page<Skills> findAll(String label, Pageable pageable);

    boolean existsByRef(String ref);

    Skills findByRef(String ref);

    boolean existsByLabel(String label);

    Skills findByLabel(String skillLabel);
    List<Skills> findByCoursesContaining(Course course);
}
