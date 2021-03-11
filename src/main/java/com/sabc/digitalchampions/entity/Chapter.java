package com.sabc.digitalchampions.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String title;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedAt;
    @Column(nullable = false, unique = true)
    private String ref;

    @ManyToOne(optional = false, cascade =CascadeType.ALL)
    private Course course;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.REMOVE,
                    CascadeType.MERGE
            },
            orphanRemoval = true,
            mappedBy = "chapter"
    )
    private List<Section> sections;

    public Chapter() {
        this.createAt = new Date();
        this.lastUpdatedAt = new Date();
        this.sections = new ArrayList<>();
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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Chapter setSections(List<Section> sections) {
        this.sections = sections;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chapter chapter = (Chapter) o;
        return id == chapter.id &&
                title.equals(chapter.title) &&
                createAt.equals(chapter.createAt) &&
                Objects.equals(lastUpdatedAt, chapter.lastUpdatedAt) &&
                ref.equals(chapter.ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, createAt, lastUpdatedAt, ref);
    }

    public String toString(){
        return title;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
