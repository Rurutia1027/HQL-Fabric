package com.hql.fabric.example.controller;

import com.hql.fabric.domain.permission.AuthGroup;
import com.hql.fabric.example.service.AuthGroupService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(value = "/api/permission", produces = "application/json")
public class AuthGroupController {
    @Autowired
    private AuthGroupService authGroupService;


    private static final Logger LOG = LoggerFactory.getLogger(AuthGroupController.class);

    @RequestMapping(value = "/authgroups", method = RequestMethod.POST)
    public AuthGroup createAuthGroup(@RequestBody AuthGroup authGroup,
                                     HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse) throws Exception {
        if (Objects.nonNull(authGroup.getName()) && Objects.nonNull(authGroupService.find(authGroup.getName()))) {
            LOG.error("Error creating AuthGroup name {} already in use", authGroup.getName());
            httpServletResponse.setStatus(HttpStatus.CONFLICT.value());
            return null;
        }

        AuthGroup newAuthGroup = null;
        newAuthGroup = authGroupService.create(authGroup);
        if (Objects.isNull(newAuthGroup)) {
            LOG.error("Error creating AuthGroup");
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return newAuthGroup;
    }
}
