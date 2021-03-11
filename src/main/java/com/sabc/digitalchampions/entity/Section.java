package com.sabc.digitalchampions.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sabc.digitalchampions.exceptions.AbstractException;
import com.sabc.digitalchampions.exceptions.NullSectionTitle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class Section extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    @Column(unique = true, updatable = false)
    private String ref;
    private Date createdAt;
    private Date lastUpdatedAt;

    @ManyToOne(optional = false)
    private Chapter chapter;

    @ManyToOne()
    @JoinColumn()
    private Section parentSection;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.REMOVE,
                    CascadeType.MERGE
            },
            orphanRemoval = true,
            mappedBy = "parentSection"
    )
    private List<Section> sections;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.REMOVE,
                    CascadeType.MERGE
            },
            orphanRemoval = true,
            mappedBy = "section"
    )
    private List<Content> contents;

    public Section() {
        this.createdAt = new Date();
        this.lastUpdatedAt = new Date();
        this.contents = new ArrayList<>();
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Section getParentSection() {
        return parentSection;
    }

    public void setParentSection(Section parentSection) {
        this.parentSection = parentSection;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public Section setChapter(Chapter chapter) {
        this.chapter = chapter;
        return this;
    }

    public List<Content> getContents() {
        return contents;
    }

    public Section setContents(List<Content> contents) {
        this.contents = contents;
        return this;
    }

    public String getRef() {
        return ref;
    }

    public Section setRef(String ref) {
        this.ref = ref;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return id == section.id &&
                title.equals(section.title) &&
                Objects.equals(createdAt, section.createdAt) &&
                Objects.equals(lastUpdatedAt, section.lastUpdatedAt) &&
                Objects.equals(parentSection, section.parentSection) &&
                Objects.equals(sections, section.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, createdAt, lastUpdatedAt, parentSection, sections);
    }

    @Override
    public void checkEntity() throws AbstractException {
        if (title == null){
            throw new NullSectionTitle();
        }
    }

    @Override
    public String toString(){
        return title;
    }
}
