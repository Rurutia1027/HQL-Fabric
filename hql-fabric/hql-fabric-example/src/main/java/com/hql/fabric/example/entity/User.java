package com.hql.fabric.example.entity;

import com.hql.fabric.persistence.entity.Artifact;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = User.TABLE)
public class User extends Artifact {
    public static final String TABLE = "test_user";

    private String name;
    private String email;


    // -- getter && setter --
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
