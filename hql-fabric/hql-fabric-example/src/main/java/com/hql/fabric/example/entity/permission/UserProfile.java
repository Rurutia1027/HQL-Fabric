package com.hql.fabric.example.entity.permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.hql.fabric.persistence.entity.NamedArtifact;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

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
@Table(name = UserProfile.TABLE)
@JsonRootName(value = UserProfile.Root)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({NamedArtifact.NameAttribute, NamedArtifact.DescriptionColumnName,
        NamedArtifact.DescriptionColumnName})
@Inheritance(strategy = InheritanceType.JOINED)
public class UserProfile extends NamedArtifact {
    public static final String TABLE = "v1_user_profile";
    public static final String Root = "user_profile";
    public static final String UNIQUE_COLUMN_NAME = "id";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = User.UNIQUE_COLUMN_NAME)
    @JsonIgnore
    private User user;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    // -- getter && setter

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
