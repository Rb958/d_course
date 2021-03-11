package com.sabc.digitalchampions.entity;

import com.sabc.digitalchampions.exceptions.AbstractException;
import com.sabc.digitalchampions.exceptions.NullSkillLabelException;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table
public class Skills extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true, updatable = false)
    private String ref;
    @Column(unique = true, nullable = false)
    private String label;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastCreatedAt;

    public Skills() {
        this.createdAt = new Date();
        this.lastCreatedAt = new Date();
    }

    public int getId() {
        return id;
    }

    public Skills setId(int id) {
        this.id = id;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public Skills setLabel(String label) {
        this.label = label;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Skills setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getLastCreatedAt() {
        return lastCreatedAt;
    }

    public Skills setLastCreatedAt(Date lastCreatedAt) {
        this.lastCreatedAt = lastCreatedAt;
        return this;
    }

    public String getRef() {
        return ref;
    }

    public Skills setRef(String ref) {
        this.ref = ref;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skills skills = (Skills) o;
        return id == skills.id &&
                label.equals(skills.label) &&
                Objects.equals(createdAt, skills.createdAt) &&
                Objects.equals(lastCreatedAt, skills.lastCreatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, createdAt, lastCreatedAt);
    }

    @Override
    public void checkEntity() throws AbstractException {
        if (label == null){
            throw new NullSkillLabelException();
        }
    }
}
