package com.sabc.digitalchampions.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sabc.digitalchampions.exceptions.AbstractException;
import com.sabc.digitalchampions.exceptions.NullCoursAuthorException;
import com.sabc.digitalchampions.exceptions.NullCoursTitleException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class Course extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String summary;
    @Column(columnDefinition = "TEXT")
    private String prerequisites;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedAt;
    private boolean published;
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedAt;
    @Column(nullable = false)
    private String author;
    @Column(unique = true, updatable = false)
    private String ref;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "course"
    )
    private List<Chapter> chapters;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = "course_skill",
            joinColumns = {
                    @JoinColumn(name = "course_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "skill_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Skills> skills;

    public Course() {
        this.createAt = new Date();
        this.lastUpdatedAt = new Date();
        this.published = false;
        this.skills = new ArrayList<>();
        this.chapters = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public String getSummary() {
        return summary;
    }

    public Course setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public List<Skills> getSkills() {
        return skills;
    }

    public Course setSkills(List<Skills> skills) {
        this.skills = skills;
        return this;
    }

    public void addSkills(Skills skills){
        this.skills.add(skills);
    }

    public void removeSkills(Skills skills){
        this.skills.remove(skills);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id &&
                published == course.published &&
                title.equals(course.title) &&
                Objects.equals(prerequisites, course.prerequisites) &&
                createAt.equals(course.createAt) &&
                Objects.equals(lastUpdatedAt, course.lastUpdatedAt) &&
                publishedAt.equals(course.publishedAt) &&
                author.equals(course.author) &&
                ref.equals(course.ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, prerequisites, createAt, lastUpdatedAt, published, publishedAt, author, ref);
    }

    @Override
    public String toString() {
        return title;
    }

    public void checkEntity() throws AbstractException {
        if (title == null){
            throw new NullCoursTitleException();
        }
        if (author == null){
            throw new NullCoursAuthorException();
        }
    }
}
