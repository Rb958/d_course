package com.sabc.digitalchampions.repository;

import com.sabc.digitalchampions.entity.Chapter;
import com.sabc.digitalchampions.entity.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
    boolean existsByTitleAndChapter_Ref(String title, String chapterRef);

    Page<Section> findAllByChapter_Ref(String chapterRef, Pageable pageable);

    boolean existsByRef(String ref);

    boolean existsByRefAndChapter_Ref(String ref, String chapterRef);

    Section findDistinctByRefAndAndChapter_Ref(String sectionRef, String chapterRef);

    Section findByRef(String ref);
}
