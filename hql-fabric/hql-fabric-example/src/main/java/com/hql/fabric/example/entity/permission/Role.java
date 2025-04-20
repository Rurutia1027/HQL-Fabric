package com.hql.fabric.example.entity.permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.hql.fabric.persistence.entity.NamedArtifact;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = Role.TABLE)
@JsonRootName(value = Role.Root)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({NamedArtifact.NameAttribute, NamedArtifact.DisplayNameAttribute,
        NamedArtifact.DescriptionColumnName})
@Inheritance(strategy = InheritanceType.JOINED)
public class Role extends NamedArtifact {
    public static final String TABLE = "v1_role";
    public static final String Root = "role";
    public static final String UNIQUE_COLUMN_NAME = "id";

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles", targetEntity = User.class)
    private List<User> users;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Permission.class)
    @JoinTable(name = "v1_roles_permissions",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = Role.UNIQUE_COLUMN_NAME),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName =
                    Permission.UNIQUE_COLUMN_NAME))
    private List<Permission> permissions;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles", targetEntity = AuthGroup.class)
    private List<AuthGroup> groups;


    // -- getter && setter --
    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @JsonIgnore
    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<AuthGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<AuthGroup> groups) {
        this.groups = groups;
    }
}
