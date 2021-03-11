package com.sabc.digitalchampions.repository;

import com.sabc.digitalchampions.entity.Chapter;
import com.sabc.digitalchampions.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findAllByCourse(Course course);

    boolean existsByRef(String chapterRef);

    Chapter findByRef(String chapterRef);

    boolean existsByTitle(String title);

    boolean existsByRefAndCourse(String chapterRef, Course course);
}
