package com.hql.fabric.domain.permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.hql.fabric.persistence.entity.NamedArtifact;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = User.TABLE)
@JsonRootName(value = User.Root)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({NamedArtifact.NameAttribute, NamedArtifact.DisplayNameAttribute,
        NamedArtifact.DescriptionColumnName})
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends NamedArtifact {
    public static final String TABLE = "v1_user";
    public static final String Root = "user";
    public static final String UNIQUE_COLUMN_NAME = "id";

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER,
            orphanRemoval = true)
    private UserProfile profile;

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
        if (profile != null) {
            profile.setUser(this);
        }
    }

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Role.class)
    @JoinTable(name = "v1_users_roles", joinColumns = @JoinColumn(name = "user_id",
            referencedColumnName = User.UNIQUE_COLUMN_NAME),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = Role.UNIQUE_COLUMN_NAME))
    @JsonIgnore
    private List<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AuthGroup.class)
    @JoinTable(name = "v1_users_groups", joinColumns = @JoinColumn(name = "user_id",
            referencedColumnName = User.UNIQUE_COLUMN_NAME),
            inverseJoinColumns =
            @JoinColumn(name = "group_id", referencedColumnName = AuthGroup.UNIQUE_COLUMN_NAME))
    @JsonIgnore
    private List<AuthGroup> groups;


    // -- getter && setter --

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


 public List<AuthGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<AuthGroup> groups) {
        this.groups = groups;
    }
}
