package com.sabc.digitalchampions.repository;

import com.sabc.digitalchampions.entity.Content;
import com.sabc.digitalchampions.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findAllBySection_Ref(String sectionRef);

    boolean existsByRef(String ref);

    Content findByRef(String ref);

    boolean existsByRefAndSection_Ref(String contentRef, String sectionRef);
}
