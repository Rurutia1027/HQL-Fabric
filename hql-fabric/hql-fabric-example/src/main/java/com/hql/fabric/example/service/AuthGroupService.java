package com.hql.fabric.example.service;

import com.hql.fabric.domain.permission.AuthGroup;
import com.hql.fabric.persistence.query.builder.HqlQueryBuilder;
import com.hql.fabric.persistence.service.IHqlQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("authGroupService")
public class AuthGroupService implements IAuthGroupService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthGroupService.class);

    @Autowired
    private IHqlQueryService hqlQueryService;

    @Override
    public Collection<AuthGroup> list() {
        LOG.info("list all [AuthGroup]");
        HqlQueryBuilder hqlQueryBuilder = new HqlQueryBuilder();
        String hql = hqlQueryBuilder
                .fromAs(AuthGroup.class, "auth_group")
                .select("auth_group")
                .build();
        Map<String, Object> hqlParams = hqlQueryBuilder.getInjectionParameters();
        hqlQueryBuilder.clear();
        LOG.info("#list hql {}, hql params len {}", hql, hqlParams.size());
        List<AuthGroup> authGroups = hqlQueryService.query(hql, hqlParams);
        return authGroups;
    }

    @Override
    public AuthGroup find(String authGroupIdOrName) {
        LOG.info("find [AuthGroup] via idOrName {}", authGroupIdOrName);
        HqlQueryBuilder hqlQueryBuilder = new HqlQueryBuilder();
        String hql = hqlQueryBuilder.fromAs(AuthGroup.class, "auth_group")
                .eq("auth_group.id", authGroupIdOrName)
                .or()
                .eq("auth_group.name", authGroupIdOrName)
                .or()
                .eq("auth_group.displayName", authGroupIdOrName)
                .build();
        Map<String, Object> hqlParams = hqlQueryBuilder.getInjectionParameters();
        hqlQueryBuilder.clear();
        LOG.info("#find hql {}, hql params len {}", hql, hqlParams.size());
        List<AuthGroup> authGroups = (List<AuthGroup>) hqlQueryService.query(hql, hqlParams);
        return authGroups == null || authGroups.isEmpty() ? null : authGroups.get(0);
    }

    @Override
    public AuthGroup delete(String authGroupIdOrName) {
        LOG.info("#delete [AuthGroup] by given authGroupIdOrName {}", authGroupIdOrName);
        AuthGroup existing = this.find(authGroupIdOrName);
        if (Objects.isNull(existing)) {
            LOG.error("#delete auth group item cannot be found via given {}", authGroupIdOrName);
            return null;
        }
        return hqlQueryService.delete(existing);
    }

    @Override
    public AuthGroup update(String authGroupIdOrName, AuthGroup authGroup) {
        return null;
    }

    @Override
    public AuthGroup replace(String authGroupIdOrName, AuthGroup authGroup) {
        return null;
    }

    @Override
    public AuthGroup create(AuthGroup authGroup) {
        if (!authGroup.hasDisplayName()) {
            LOG.warn("create AuthGroup does not have display name assign name to it!");
            authGroup.setDisplayName(authGroup.getName());
        }
        return hqlQueryService.save(authGroup);
    }
}
