package com.sabc.digitalchampions.services;

import com.sabc.digitalchampions.entity.*;
import com.sabc.digitalchampions.exceptions.*;
import com.sabc.digitalchampions.repository.ChapterRepository;
import com.sabc.digitalchampions.repository.ContentRepository;
import com.sabc.digitalchampions.repository.CourseRepository;
import com.sabc.digitalchampions.repository.SectionRepository;
import com.sabc.digitalchampions.utils.codegenerator.CodeConfig;
import com.sabc.digitalchampions.utils.codegenerator.CodeConfigBuilder;
import com.sabc.digitalchampions.utils.codegenerator.RbCodeGenerator;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final SectionRepository sectionRepository;
    private final ContentRepository contentRepository;
    private final SkillService skillService;

    public CourseService(CourseRepository courseRepository,
                         ChapterRepository chapterRepository,
                         SectionRepository sectionRepository,
                         ContentRepository contentRepository,
                         SkillService skillService
    ) {
        this.courseRepository = courseRepository;
        this.chapterRepository = chapterRepository;
        this.sectionRepository = sectionRepository;
        this.contentRepository = contentRepository;
        this.skillService = skillService;
    }

    private String genRef(String pref) {
        CodeConfig chapterCodeConfig = (new CodeConfigBuilder())
                .setLength(12)
                .setPrefix(pref.concat("_"))
                .build();
        RbCodeGenerator refGen = new RbCodeGenerator(chapterCodeConfig);
        return refGen.generate();
    }

    // Manage course Entity

    public Course saveCourse(Course course) throws AbstractException {
        if (courseRepository.existsByTitle(course.getTitle())){
            throw new CourseExistException();
        }
        course.setRef(genRef("CRS"));
        System.out.println(course);
        if (course.getChapters() != null && !course.getChapters().isEmpty()) {
            course.getChapters().forEach(chapter -> {
                chapter.setRef(genRef("CHP"));
                chapter.setCourse(course);
                if (chapter.getSections() != null && !chapter.getSections().isEmpty()){
                    chapter.getSections().forEach(section -> {
                        section.setRef(genRef("SEC"));
                        section.setChapter(chapter);
                        if (section.getContents() != null && !section.getContents().isEmpty()){
                            section.getContents().forEach(content -> {
                                content.setRef(genRef("CNT"));
                                content.setSection(section);
                            });
                        }
                        if (section.getSections() != null && !section.getSections().isEmpty()){
                            section.getSections().forEach(subSection -> {
                                subSection.setRef(genRef("SEC"));
                                subSection.setParentSection(section);
                            });
                        }
                    });
                }
            });
        }
        return courseRepository.save(course);
    }

    public Page<Course> findCourses(String key, Pageable pageable){
        return courseRepository.findAll(key, pageable);
    }

    public Course findCourseByRef(String ref) throws CourseNotFoundException {
        if (!courseRepository.existsByRef(ref)){
            throw new CourseNotFoundException();
        }
        return courseRepository.findByRef(ref);
    }

    public boolean deleteCourse(String ref) throws CourseNotFoundException {
        if (!courseRepository.existsByRef(ref)) {
            throw new CourseNotFoundException();
        }
        Course tmp = courseRepository.findByRef(ref);
        courseRepository.delete(tmp);
        return !courseRepository.existsByRef(ref);
    }

    public boolean publishCourse(String ref) throws CourseNotFoundException {
        if (!courseRepository.existsByRef(ref)){
            throw new CourseNotFoundException();
        }
        Course tmpCourse = courseRepository.findByRef(ref);
        tmpCourse.setPublished(true);
        tmpCourse.setPublishedAt(new Date());
        tmpCourse = courseRepository.save(tmpCourse);
        return tmpCourse.isPublished();
    }

    public boolean disableCourse(String ref) throws CourseNotFoundException {
        if (!courseRepository.existsByRef(ref)){
            throw new CourseNotFoundException();
        }
        Course tmpCourse = courseRepository.findByRef(ref);
        tmpCourse.setPublished(false);
        tmpCourse.setPublishedAt(null);
        tmpCourse = courseRepository.save(tmpCourse);
        return !tmpCourse.isPublished();
    }

    public Course updateCourse(String ref,Course course) throws CourseExistException, CourseNotFoundException {
        if (!courseRepository.existsByRef(ref)){
            throw new CourseNotFoundException();
        }

        Course tmpCourse = courseRepository.findByRef(ref);
        if (!course.getTitle().equals(tmpCourse.getTitle()) && courseRepository.existsByTitle(course.getTitle())){
            throw new CourseExistException();
        }
        course.setLastUpdatedAt(new Date());
        course.setId(tmpCourse.getId());
        course.setRef(tmpCourse.getRef());
        return courseRepository.save(course);
    }

    public Page<Course> findPublishedCourse(Pageable pageable){
        return courseRepository.findAllByPublished(true, pageable);
    }

    // Manage Chapter Entity

    public Chapter saveChapter(String courseRef, Chapter chapter) throws AbstractException {
        if (!courseRepository.existsByRef(courseRef)){
            throw new CourseNotFoundException();
        }

        Course course = courseRepository.findByRef(courseRef);

        if (chapterRepository.existsByTitle(chapter.getTitle())){
            throw new ChapterExistException();
        }

        chapter.setCourse(course);
        chapter.setRef(genRef("CHP"));
        return chapterRepository.save(chapter);
    }

    public List<Chapter> findChaptersOf(String courseRef) throws CourseNotFoundException {
        if (!courseRepository.existsByRef(courseRef)){
            throw new CourseNotFoundException();
        }
        Course course = courseRepository.findByRef(courseRef);
        return chapterRepository.findAllByCourse(course);
    }

    public Chapter findChapterByRef(String courseRef, String chapterRef) throws AbstractException {

        if (!courseRepository.existsByRef(courseRef)){
            throw new CourseNotFoundException();
        }

        if (!chapterRepository.existsByRef(chapterRef)){
            throw new ChapterNotFoundException();
        }
        Course course = courseRepository.findByRef(courseRef);
        if (!chapterRepository.existsByRefAndCourse(chapterRef, course)){
            throw new ChapterOwnerException();
        }

        return chapterRepository.findByRef(chapterRef);
    }

    public boolean deleteChapter(String courseRef, String chapterRef) throws AbstractException {
        if (!courseRepository.existsByRef(courseRef)){
            throw new CourseNotFoundException();
        }
        if (!chapterRepository.existsByRef(chapterRef)){
            throw new ChapterNotFoundException();
        }

        Course course = courseRepository.findByRef(courseRef);

        if (!chapterRepository.existsByRefAndCourse(chapterRef, course)){
            throw new ChapterOwnerException();
        }

        Chapter chapter = chapterRepository.findByRef(chapterRef);
        chapterRepository.delete(chapter);
        return !chapterRepository.existsByRef(chapterRef);
    }

    public Chapter updateChapter(String courseRef, String chapterRef, Chapter chapter) throws AbstractException {
        if (!courseRepository.existsByRef(courseRef)){
            throw new CourseNotFoundException();
        }

        if (!chapterRepository.existsByRef(chapterRef)){
            throw new ChapterNotFoundException();
        }

        Course course = courseRepository.findByRef(courseRef);

        if (!chapterRepository.existsByRefAndCourse(chapterRef, course)){
            throw new ChapterOwnerException();
        }

        Chapter tmpChapter = chapterRepository.findByRef(chapterRef);
        chapter.setId(tmpChapter.getId());
        chapter.setRef(tmpChapter.getRef());
        chapter.setCourse(tmpChapter.getCourse());
        chapter.setCreateAt(tmpChapter.getCreateAt());
        chapter.setId(tmpChapter.getId());
        chapter.setLastUpdatedAt(new Date());
        return chapterRepository.save(chapter);
    }

    // Manage Section
    public Section saveSection(String chapterRef, Section section) throws AbstractException{

        if (!chapterRepository.existsByRef(chapterRef)){
            throw new ChapterNotFoundException();
        }

        Chapter chapter = chapterRepository.findByRef(chapterRef);

        if (sectionRepository.existsByTitleAndChapter_Ref(section.getTitle(), chapterRef)){
            throw new SectionExistException();
        }

        section.setRef(genRef("SEC"));
        section.setChapter(chapter);
        return sectionRepository.save(section);
    }

    public List<Section> findSectionsByChapter(String chapterRef) throws AbstractException {
        if (!chapterRepository.existsByRef(chapterRef)){
            throw new ChapterNotFoundException();
        }
        return sectionRepository.findAllByChapter_Ref(chapterRef);
    }

    public Section updateSection(String chapterRef, String ref, Section section) throws AbstractException {
        if (!chapterRepository.existsByRef(chapterRef)){
            throw new ChapterNotFoundException();
        }

        if (!sectionRepository.existsByRef(ref)){
            throw new SectionNotFoundException();
        }

        Section section1 = sectionRepository.findByRef(ref);
        System.out.println(section1);

        if (!sectionRepository.existsByRefAndChapter_Ref(ref, chapterRef)){
            throw new SectionOwnerException();
        }

        if (!section1.getTitle().equals(section.getTitle()) && sectionRepository.existsByTitleAndChapter_Ref(section.getTitle(),chapterRef)){
            throw new SectionExistException();
        }

        Chapter chapter = chapterRepository.findByRef(chapterRef);

        section.setChapter(chapter);
        section.setId(section1.getId());
        section.setCreatedAt(section1.getCreatedAt());
        section.setLastUpdatedAt(new Date());
        return sectionRepository.save(section);
    }

    public Section findSectionByRef(String ref) throws AbstractException {
        if (!sectionRepository.existsByRef(ref)){
            throw new SectionNotFoundException();
        }
        return sectionRepository.findByRef(ref);
    }

    public boolean deleteSectionByRef(String chapterRef, String sectionref) throws AbstractException {
        if(!chapterRepository.existsByRef(chapterRef)){
            throw new ChapterNotFoundException();
        }
        if (!sectionRepository.existsByRef(sectionref)){
            throw new SectionNotFoundException();
        }

        if (!sectionRepository.existsByRefAndChapter_Ref(sectionref, chapterRef)){
            throw new SectionOwnerException();
        }

        Section section = sectionRepository.findByRef(sectionref);
        sectionRepository.delete(section);
        return !sectionRepository.existsByRef(sectionref);
    }

    public Section saveSubSection(String sectionRef, Section section) throws AbstractException {
        if (!sectionRepository.existsByRef(sectionRef)){
            throw new SectionNotFoundException();
        }

        Section parent = sectionRepository.findByRef(sectionRef);
        section.setParentSection(parent);
        section.setRef(genRef("SEC"));
        return sectionRepository.save(section);
    }
    // Manage Content Entity

    public Content saveCContent(String sectionRef, Content content) throws NullSectionReferenceException, SectionNotFoundException {
        if (!sectionRepository.existsByRef(sectionRef)){
            throw new SectionNotFoundException();
        }

        Section section = sectionRepository.findByRef(sectionRef);
        content.setSection(section);
        content.setRef(genRef("CNT"));
        return contentRepository.save(content);
    }

    public List<Content> findContentBySection(String sectionRef) throws SectionNotFoundException {
        if (!sectionRepository.existsByRef(sectionRef)){
            throw new SectionNotFoundException();
        }
        return contentRepository.findAllBySection_Ref(sectionRef);
    }

    public Content findContentByRef(String sectionRef, String ref) throws AbstractException {
        if (!contentRepository.existsByRef(ref)){
            throw new ContentNotFoundException();
        }

        if (!contentRepository.existsByRefAndSection_Ref(ref, sectionRef)){
            throw new ContentOwnerException();
        }
        return contentRepository.findByRef(ref);
    }

    public boolean deleteContent(String sectionRef, String contentRef) throws AbstractException {
        if (!contentRepository.existsByRef(contentRef)){
            throw new ContentNotFoundException();
        }

        if (!contentRepository.existsByRefAndSection_Ref(contentRef, sectionRef)){
            throw new ContentOwnerException();
        }

        Content content = contentRepository.findByRef(contentRef);

        contentRepository.delete(content);
        return !contentRepository.existsByRef(contentRef);
    }

    public Content updateContent(String sectionRef, String contentRef, Content content) throws AbstractException {

        if (!contentRepository.existsByRef(contentRef)){
            throw new ContentNotFoundException();
        }

        if (!sectionRepository.existsByRef(sectionRef)){
            throw new SectionNotFoundException();
        }

        if (!contentRepository.existsByRefAndSection_Ref(contentRef, sectionRef)){
            throw new ContentOwnerException();
        }

        Section section = sectionRepository.findByRef(sectionRef);
        Content tmpContent = contentRepository.findByRef(contentRef);

        content.setId(tmpContent.getId());
        content.setSection(section);
        return contentRepository.save(content);
    }

    @Transactional
    public boolean setSkills(String courseRef, List<String> skills) throws AbstractException{
        if (!courseRepository.existsByRef(courseRef)){
            throw new CourseNotFoundException();
        }

        Course course = courseRepository.findByRef(courseRef);
        List<Skills> skillsList = new ArrayList<>();
        for (String skillLabel: skills) {
            if(skillService.existsByLabel(skillLabel)){
                Skills skill = skillService.findByLabel(skillLabel);
                skillsList.add(skill);
                course.addSkills(skill);
            }
        }
        course = courseRepository.save(course);
        return skillService.findAllByCourse(course).containsAll(skillsList);
    }

    public List<Skills> getSkills(String courseRef) throws AbstractException{
        if (!courseRepository.existsByRef(courseRef)){
            throw new CourseNotFoundException();
        }
        Course course = courseRepository.findByRef(courseRef);
        return skillService.findAllByCourse(course);
    }

    public boolean removeSkills(String courseRef, List<String> skills) throws AbstractException{
        if (!courseRepository.existsByRef(courseRef)){
            throw new CourseNotFoundException();
        }

        Course course = courseRepository.findByRef(courseRef);

        List<Skills> skillsList = new ArrayList<>();
        for (String skillLabel: skills) {
            if(skillService.existsByLabel(skillLabel)) {
                Skills skill = skillService.findByLabel(skillLabel);
                skillsList.add(skill);
                course.removeSkills(skill);
            }
        }
        course = courseRepository.save(course);
        return !skillService.findAllByCourse(course).containsAll(skillsList);
    }

    public boolean addSkill(String ref, Skills skills) throws AbstractException{
        if (!courseRepository.existsByRef(ref)){
            throw new CourseNotFoundException();
        }
        if (!skillService.existByRef(skills.getRef())){
            throw new SkillNotFoundException();
        }
        Skills skills1 = skillService.findByRef(skills.getRef());
        Course course = courseRepository.findByRef(ref);
        course.addSkills(skills1);
        course = courseRepository.save(course);
        return skillService.findAllByCourse(course).contains(skills1);
    }

    public boolean removeSkill(String ref, Skills skills) throws AbstractException{
        if (!courseRepository.existsByRef(ref)){
            throw new CourseNotFoundException();
        }
        if (!skillService.existByRef(skills.getRef())){
            throw new SkillNotFoundException();
        }
        Course course = courseRepository.findByRef(ref);
        Skills skills1 = skillService.findByRef(skills.getRef());
        course.removeSkills(skills1);
        course = courseRepository.save(course);
        return !skillService.findAllByCourse(course).contains(skills1);
    }

}
