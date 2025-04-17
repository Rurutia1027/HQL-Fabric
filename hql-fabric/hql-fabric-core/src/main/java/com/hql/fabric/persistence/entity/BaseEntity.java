package com.hql.fabric.persistence.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity {
    // unique identifier
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected String id;

    // Metadata fields
    protected Date createdDate;
    protected Date modifiedDate;
    protected Long versionNumber = 1L;
    protected boolean locked;
    protected boolean disabled;

    // Default Constructor
    public BaseEntity() {
        this.createdDate = new Date();
        this.modifiedDate = this.createdDate;
    }

    // Copy Constructor
    public BaseEntity(BaseEntity rhs) {
        this.id = rhs.id;
        this.createdDate = rhs.createdDate;
        this.modifiedDate = rhs.modifiedDate;
        this.versionNumber = rhs.versionNumber;
        this.locked = rhs.locked;
        this.disabled = rhs.disabled;
    }

    // -- getter && setter --
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
