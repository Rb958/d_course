package com.sabc.digitalchampions.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true, updatable = false)
    private String ref;
    @Column(columnDefinition = "TEXT")
    private String text;
    private String imageLink;
    private String videoLink;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatesAt;

    @ManyToOne(optional = false)
    private Section section;

    public Content() {
        this.createdAt = new Date();
        this.lastUpdatesAt = new Date();
    }

    public long getId() {
        return id;
    }

    public Content setId(long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public Content setText(String text) {
        this.text = text;
        return this;
    }

    public String getImageLink() {
        return imageLink;
    }

    public Content setImageLink(String imageLink) {
        this.imageLink = imageLink;
        return this;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public Content setVideoLink(String videoLink) {
        this.videoLink = videoLink;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Content setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getLastUpdatesAt() {
        return lastUpdatesAt;
    }

    public Content setLastUpdatesAt(Date lastUpdatesAt) {
        this.lastUpdatesAt = lastUpdatesAt;
        return this;
    }

    public Section getSection() {
        return section;
    }

    public Content setSection(Section section) {
        this.section = section;
        return this;
    }

    public String getRef() {
        return ref;
    }

    public Content setRef(String ref) {
        this.ref = ref;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return id == content.id &&
                Objects.equals(text, content.text) &&
                Objects.equals(imageLink, content.imageLink) &&
                Objects.equals(videoLink, content.videoLink) &&
                Objects.equals(createdAt, content.createdAt) &&
                Objects.equals(lastUpdatesAt, content.lastUpdatesAt) &&
                Objects.equals(section, content.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, imageLink, videoLink, createdAt, lastUpdatesAt, section);
    }
}
