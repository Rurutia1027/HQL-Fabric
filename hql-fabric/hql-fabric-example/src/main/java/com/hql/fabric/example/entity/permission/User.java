package com.hql.fabric.example.entity.permission;

import com.hql.fabric.persistence.entity.NamedArtifact;
import jakarta.persistence.Entity;
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
@Table(name = User.TABLE)
public class User extends NamedArtifact {
    public static final String TABLE = "example_user";
    public static final String Root = "user";
}
