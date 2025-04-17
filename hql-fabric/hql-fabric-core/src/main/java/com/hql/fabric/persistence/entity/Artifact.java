package com.hql.fabric.persistence.entity;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class Artifact extends BaseEntity {
    protected String name;
    protected String displayName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
