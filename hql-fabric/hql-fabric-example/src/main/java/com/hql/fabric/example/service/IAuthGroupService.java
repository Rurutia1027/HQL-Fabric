package com.hql.fabric.example.service;

import com.hql.fabric.example.entity.permission.AuthGroup;
import com.hql.fabric.persistence.entity.NamedArtifact;

import java.util.Collection;

public interface IAuthGroupService<T extends NamedArtifact> {
    Collection<AuthGroup> list();

    AuthGroup find(String authGroupIdOrName);

    AuthGroup delete(String authGroupIdOrName);

    AuthGroup update(String authGroupIdOrName, AuthGroup authGroup);

    AuthGroup replace(String authGroupIdOrName, AuthGroup authGroup);

    AuthGroup create(AuthGroup authGroup);
}
