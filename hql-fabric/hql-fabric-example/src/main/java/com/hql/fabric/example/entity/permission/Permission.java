package com.hql.fabric.example.entity.permission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.hql.fabric.persistence.entity.NamedArtifact;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

/**
 * * <p>
 * * User <-> UserProfile => 1:1
 * * User <-> Role => M:N
 * * Role <-> Permission => M:N
 * * User <-> Group => M:N
 * * Group <-> Role => M: N
 */

@Data
@Entity
@Table(name = Permission.TABLE)
@JsonRootName(value = Permission.Root)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({NamedArtifact.NameAttribute, NamedArtifact.DisplayNameAttribute,
        NamedArtifact.DescriptionColumnName})
@Inheritance(strategy = InheritanceType.JOINED)
public class Permission extends NamedArtifact {
    public static final String TABLE = "v1_permission";
    public static final String Root = "permission";
    public static final String UNIQUE_COLUMN_NAME = "id";

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions", targetEntity = Role.class)
    private List<Role> roles;

    // -- getter && setter --

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
