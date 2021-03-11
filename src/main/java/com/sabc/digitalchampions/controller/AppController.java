package com.sabc.digitalchampions.controller;

import com.sabc.digitalchampions.entity.Chapter;
import com.sabc.digitalchampions.entity.Content;
import com.sabc.digitalchampions.entity.Course;
import com.sabc.digitalchampions.entity.Section;
import com.sabc.digitalchampions.exceptions.*;
import com.sabc.digitalchampions.security.payload.response.ResponseException;
import com.sabc.digitalchampions.security.payload.response.ResponseModel;
import com.sabc.digitalchampions.services.CourseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AppController {
    private CourseService courseService;

    private Logger logger = LogManager.getLogger(AppController.class);

    public AppController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/course")
    public ResponseEntity<?> createCourse(@RequestBody @Valid Course course){
        try{
            course.checkEntity();
            return ResponseEntity.ok().body(
                    new ResponseModel<>(courseService.saveCourse(course))
            );
        } catch (AbstractException e) {
            return ResponseEntity.ok().body(
                    new ResponseException(e)
            );
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(500).body(
              new ResponseModel<>("An error occurred while trying to save a new course", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    @GetMapping("/course")
    public ResponseEntity<?> getCourses(@RequestParam(name = "title") Optional<String> key, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size){
        return ResponseEntity.ok(
                new ResponseModel<>(courseService.findCourses(key.orElse(""), PageRequest.of(page,size))));
    }

    @GetMapping("/course/{ref}")
    public ResponseEntity<?> getCourseByRef(@PathVariable(name = "ref") String ref){
        try {
            return ResponseEntity.ok().body(
                    new ResponseModel<>(
                            courseService.findCourseByRef(ref)
                    )
            );
        } catch (AbstractException e) {
            return ResponseEntity.ok().body(
                    new ResponseException(e)
            );
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PutMapping("/course/{ref}")
    public ResponseEntity<?> updateCourse(@RequestBody @Valid Course course, @PathVariable(name = "ref") String ref){
        try {
            course.checkEntity();
            return ResponseEntity.ok().body(
                    new ResponseModel<>(courseService.updateCourse(ref, course))
            );
        } catch (AbstractException e) {
            return ResponseEntity.ok(
              new ResponseException(e)
            );
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return ResponseEntity.ok(
                    new ResponseModel<>("An error occurred while tying to update course with reference" + ref + ". Please contact our support if the problem persist")
            );
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping("/course/{ref}")
    public ResponseEntity<?> deleteCourse(@PathVariable(name = "ref") String ref){
        try {
            if (courseService.deleteCourse(ref)){
                return ResponseEntity.ok(
                  new ResponseModel<>("The course was Successfully deleted")
                );
            }else{
                return ResponseEntity.ok(
                        new ResponseException(new UnknowErrorException("An error occurred while trying to delete course with reference "+ ref + ". Please contact our support if the problem persist", 500))
                );
            }
        } catch (CourseNotFoundException e) {
            return ResponseEntity.ok(
              new ResponseException(e)
            );
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(500).body(
                    new ResponseModel<>("An error occurred while trying to delete course with reference "+ ref + ". Please contact our support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PatchMapping("/course/{ref}/publish")
    public ResponseEntity<?> publishCourse(@PathVariable(name = "ref") String ref){
        try {
            if (courseService.publishCourse(ref)){
                return ResponseEntity.ok(
                        new ResponseModel<>("The course was Successfully published")
                );
            }else{
                return ResponseEntity.ok(
                        new ResponseException(new UnknowErrorException("An error occurred while trying to publish course with reference "+ ref + ". Please contact our support if the problem persist", 500))
                );
            }
        } catch (CourseNotFoundException e) {
            return ResponseEntity.ok(
                    new ResponseException(e)
            );
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(500).body(
                    new ResponseModel<>("An error occurred while trying to publish course with reference "+ ref + ". Please contact our support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PatchMapping("/course/{ref}/disable")
    public ResponseEntity<?> disableCourse(@PathVariable(name = "ref") String ref){
        try {
            if (courseService.disableCourse(ref)){
                return ResponseEntity.ok(
                        new ResponseModel<>("The course was Successfully disabled")
                );
            }else{
                return ResponseEntity.ok(
                        new ResponseException(new UnknowErrorException("An error occurred while trying to disable course with reference "+ ref + ". Please contact our support if the problem persist", 500))
                );
            }
        } catch (CourseNotFoundException e) {
            return ResponseEntity.ok(
                    new ResponseException(e)
            );
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(500).body(
                    new ResponseModel<>("An error occurred while trying to disable course with reference "+ ref + ". Please contact our support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/course/{courseRef}/chapter")
    public ResponseEntity<?> saveChapter(@PathVariable(name = "courseRef") String courseRef, @RequestBody @Valid Chapter chapter){
        try {
            return ResponseEntity.ok(new ResponseModel<>(courseService.saveChapter(courseRef, chapter)));
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        }catch(Exception e){
            return ResponseEntity.status(500).body(new ResponseModel<>(
                    "An error occurred while trying to create new chapter",
                    HttpStatus.INTERNAL_SERVER_ERROR
            ));
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PutMapping("/course/{courseRef}/chapter/{chapterRef}")
    public ResponseEntity<?> updateChapter(@PathVariable(name = "courseRef") String courseRef, @PathVariable(name = "chapterRef") String chapterRef, @RequestBody @Valid Chapter chapter){
        try {
            return ResponseEntity.ok(new ResponseModel<>(courseService.updateChapter(courseRef, chapterRef, chapter)));
        } catch (AbstractException e) {
            return ResponseEntity.ok(
                    new ResponseException(e)
            );
        }catch (Exception e){
            return ResponseEntity.status(500).body(
                    new ResponseModel<>("And error occurred while trying to update this chapter. Please contact our support if the problem persist")
            );
        }
    }

    @GetMapping("/course/{courseRef}/chapter")
    public ResponseEntity<?> getCourseChapters(@PathVariable(name = "courseRef") String courseRef){
        try {
            return ResponseEntity.ok(new ResponseModel<>(courseService.findChaptersOf(courseRef)));
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        }
    }

    @GetMapping("/course/{courseRef}/chapter/{chapterRef}")
    public ResponseEntity<?> getSpecificChapter(@PathVariable(name = "courseRef") String courseRef, @PathVariable(name = "chapterRef") String chapterRef){
        try {
            return ResponseEntity.ok(new ResponseModel<>(courseService.findChapterByRef(courseRef, chapterRef)));
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping("/course/{courseRef}/chapter/{chapterRef}")
    public ResponseEntity<?> deleteChapter(@PathVariable(name = "courseRef") String courseRef, @PathVariable(name = "chapterRef") String chapterRef){
        try {
            if (courseService.deleteChapter(courseRef, chapterRef)) {
                return ResponseEntity.ok(new ResponseModel<>("Succesfully deleted", HttpStatus.OK));
            }else{
                return ResponseEntity.status(500).body(new ResponseModel<>("An error occurred while trying to delete this chapter. Please contact our support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR));
            }
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(500).body(new ResponseModel<>("An error occurred while trying to delete this chapter. Please contact our support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/chapter/{chapterRef}/section")
    public ResponseEntity<?> createSection(@PathVariable(name = "chapterRef") String chapterRef, @RequestBody @Valid Section section){
        try {
            section.checkEntity();
            return ResponseEntity.ok(new ResponseModel<>(courseService.saveSection(chapterRef, section)));
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(500).body(new ResponseModel<>("An error occurred while trying to create a section into the chapter with ref : " + chapterRef + ". Please contact our support if the problem persist",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PutMapping("/chapter/{chapterRef}/section/{sectionRef}")
    public ResponseEntity<?> updateSection(@PathVariable(name = "chapterRef") String chapterRef, @PathVariable(name = "sectionRef") String sectionRef , @RequestBody @Valid Section section){
        try {
            section.checkEntity();
            return ResponseEntity.ok(new ResponseModel<>(courseService.updateSection(chapterRef, sectionRef, section)));
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(500).body(new ResponseModel<>("An error occurred while trying to update a section of chapter with ref : " + chapterRef + ". Please contact our support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/chapter/{chapterRef}/section")
    public ResponseEntity<?> getChaptersSections(@PathVariable(name = "chapterRef") String chapterRef, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size){
        try {
            return ResponseEntity.ok(new ResponseModel<>(courseService.findSectionsByChapter(chapterRef, PageRequest.of(page, size))));
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        }
    }

    @GetMapping("/chapter/{chapterRef}/section/{sectionRef}")
    public ResponseEntity<?> getSpecificSection(@PathVariable(name = "chapterRef") String chapterRef, @PathVariable(name = "sectionRef") String sectionRef){
        try {
            return ResponseEntity.ok(new ResponseModel<>(courseService.findSectionByRef(chapterRef, sectionRef)));
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping("/chapter/{chapterRef}/section/{sectionRef}")
    public ResponseEntity<?> deleteSpecificSection(@PathVariable(name = "chapterRef") String chapterRef, @PathVariable(name = "sectionRef") String sectionRef){
        try {
            if (courseService.deleteSectionByRef(chapterRef, sectionRef)) {
                return ResponseEntity.ok(new ResponseModel<>("Successfully deleted", HttpStatus.OK));
            }else {
                return ResponseEntity.status(500).body(new ResponseModel<>("An error occurred while trying to delete section. Please contact our support if this problem persist", HttpStatus.INTERNAL_SERVER_ERROR));
            }
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/section/{sectionRef}/content")
    public ResponseEntity<?> saveContent(@PathVariable(name = "sectionRef") String sectionRef, @RequestBody @Valid Content content){
        try {
            return ResponseEntity.ok(new ResponseModel<>(courseService.saveCContent(sectionRef, content)));
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        } catch (Exception e) {
           return ResponseEntity.status(500).body(
             new ResponseModel<>("An error occurred while trying to save content. Please contact our support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR)
           );
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PutMapping("/section/{sectionRef}/content/{contentRef}")
    public ResponseEntity<?> updateContent(@PathVariable(name = "sectionRef") String sectionRef,@PathVariable(name = "contentRef") String contentRef, @RequestBody @Valid Content content){
        try {
            return ResponseEntity.ok(new ResponseModel<>(courseService.updateContent(sectionRef, contentRef, content)));
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ResponseModel<>("An error occurred while trying to update content. Please contact our support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping("/section/{sectionRef}/content/{contentRef}")
    public ResponseEntity<?> deleteContent(@PathVariable(name = "sectionRef") String sectionRef,@PathVariable(name = "contentRef") String contentRef){
        try {
            if (courseService.deleteContent(sectionRef, contentRef)) {
                return ResponseEntity.ok(new ResponseModel<>("Successfully deleted", HttpStatus.OK));
            }else {
                return ResponseEntity.status(500).body(new ResponseModel<>("Successfully deleted", HttpStatus.INTERNAL_SERVER_ERROR));
            }
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ResponseModel<>("An error occurred while trying to update content. Please contact our support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    @GetMapping("/section/{sectionRef}/content/{contentRef}")
    public ResponseEntity<?> getSpecificContent(@PathVariable(name = "sectionRef") String sectionRef,@PathVariable(name = "contentRef") String contentRef){
        try {
                return ResponseEntity.ok(new ResponseModel<>(courseService.findContentByRef(sectionRef, contentRef)));
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        }
    }

    @GetMapping("/section/{sectionRef}/content")
    public ResponseEntity<?> getContents(@PathVariable(name = "sectionRef") String sectionRef){
        try {
            return ResponseEntity.ok(new ResponseModel<>(courseService.findContentBySection(sectionRef)));
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/section/{sectionRef}/subsection")
    public ResponseEntity<?> createSubSection(@PathVariable(name = "sectionRef") String sectionRef, @RequestBody @Valid Section section){
        try {
            section.checkEntity();
            return ResponseEntity.ok(new ResponseModel<>(courseService.saveSubSection(sectionRef, section)));
        } catch (AbstractException e) {
            return ResponseEntity.ok(new ResponseException(e));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(500).body(new ResponseModel<>("An error occurred while trying to create a subsection into the chapter with ref : " + sectionRef + ". Please contact our support if the problem persist",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
