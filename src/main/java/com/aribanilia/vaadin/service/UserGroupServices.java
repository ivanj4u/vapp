/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.service;

import com.aribanilia.vaadin.dao.UserGroupDao;
import com.aribanilia.vaadin.entity.TblUserGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserGroupServices {
    @Autowired private UserGroupDao daoUserGroup;

    private static final Logger logger = LoggerFactory.getLogger(UserGroupServices.class);

    public List<TblUserGroup> getUserGroup(String username) throws Exception {
        List<TblUserGroup> list = null;
        try {
            list = daoUserGroup.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }
}