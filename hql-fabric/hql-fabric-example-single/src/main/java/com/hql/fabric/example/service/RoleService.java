package com.hql.fabric.example.service;

import com.hql.fabric.persistence.service.IHqlQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("roleService")
public class RoleService implements IRoleService {
    @Autowired
    private IHqlQueryService hqlQueryService;
}
