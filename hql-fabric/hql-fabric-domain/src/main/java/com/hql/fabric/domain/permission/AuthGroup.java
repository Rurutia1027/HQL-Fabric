package com.hql.fabric.domain.permission;

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
@Table(name = AuthGroup.TABLE)
@JsonRootName(value = AuthGroup.Root)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({NamedArtifact.NameAttribute, NamedArtifact.DisplayNameAttribute,
        NamedArtifact.DescriptionColumnName})
@Inheritance(strategy = InheritanceType.JOINED)
public class AuthGroup extends NamedArtifact {
    public static final String TABLE = "v1_auth_group";
    public static final String Root = "auth_group";
    public static final String UNIQUE_COLUMN_NAME = "id";

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups", targetEntity = User.class)
    private List<User> users;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Role.class)

    @JoinTable(name = "v1_groups_roles",
            joinColumns = @JoinColumn(name = "group_id", referencedColumnName = AuthGroup.UNIQUE_COLUMN_NAME),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = Role.UNIQUE_COLUMN_NAME))
    private List<Role> roles;

    // -- getter && setter --

    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    @JsonIgnore
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
