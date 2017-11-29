/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.service;

import com.aribanilia.vaadin.container.JoinUserGroup;
import com.aribanilia.vaadin.dao.GroupDao;
import com.aribanilia.vaadin.dao.UserDao;
import com.aribanilia.vaadin.dao.UserGroupDao;
import com.aribanilia.vaadin.entity.TblGroup;
import com.aribanilia.vaadin.entity.TblUser;
import com.aribanilia.vaadin.entity.TblUserGroup;
import com.aribanilia.vaadin.util.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserGroupServices {
    @Autowired private UserGroupDao daoUserGroup;
    @Autowired private UserDao daoUser;
    @Autowired private GroupDao daoGroup;

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
    public List<TblUserGroup> getUserGroup(String groupId, String username) throws Exception {
        List<TblUserGroup> list = null;
        try {
            if (ValidationHelper.validateValueNotNull(username) && ValidationHelper.validateValueNotNull(groupId)) {
                list = daoUserGroup.findByGroupIdAndUsername(groupId, username);
            } else if (ValidationHelper.validateValueNotNull(username)) {
                list = daoUserGroup.findByUsername(username);
            } else if (ValidationHelper.validateValueNotNull(username)) {
                list = daoUserGroup.findByGroupId(groupId);
            } else {
                list = daoUserGroup.findAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }

    public List<JoinUserGroup> searchUserGroup(String groupId, String username) throws Exception {
        List<JoinUserGroup> list = new ArrayList<>();
        try {
            List<TblUserGroup> userGroups = getUserGroup(groupId, username);
            for (TblUserGroup userGroup : userGroups) {
                TblUser user = daoUser.findOne(userGroup.getUsername());
                TblGroup group = daoGroup.findOne(userGroup.getGroupId());
                list.add(new JoinUserGroup(group, user, userGroup));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }

    public void save(TblUserGroup userGroup) {
        daoUserGroup.save(userGroup);
    }

    public void delete(TblUserGroup userGroup) {
        daoUserGroup.delete(userGroup);
    }
}
