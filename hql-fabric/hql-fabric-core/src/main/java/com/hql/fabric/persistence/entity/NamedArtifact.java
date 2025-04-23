package com.hql.fabric.persistence.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.util.StringUtils;

@MappedSuperclass
public class NamedArtifact extends BaseEntity {
    /**
     * Name column identifier
     */
    public static final String NameColumn = "NAME";
    /**
     * Name attribute name
     */
    public static final String NameAttribute = "name";
    /**
     * Display Name column identifier
     */
    public static final String DisplayNameColumn = "DISPLAY_NAME";
    /**
     * Display Name attribute name
     */
    public static final String DisplayNameAttribute = "displayName";
    /**
     * Description attribute name
     */
    public static final String DescriptionColumnName = "description";

    @Column(name = "name")
    protected String name;

    @Column(name = "display_name")
    protected String displayName;

    protected String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean hasDisplayName() {
        return StringUtils.hasText(this.displayName);
    }
}
