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


    public List<TblUserGroup> getUserGroupByGroupId(String groupId) throws Exception {
        List<TblUserGroup> list = new ArrayList<>();
        try {
            if (ValidationHelper.validateValueNotNull(groupId)) {
                list = daoUserGroup.findByGroupId(new Long(groupId));
            } else {
                list = daoUserGroup.findAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }

    public List<TblUserGroup> getUserGroupByUsername(String username) throws Exception {
        List<TblUserGroup> list = new ArrayList<>();
        try {
            if (ValidationHelper.validateValueNotNull(username)) {
                list = daoUserGroup.findByUsername(username);
            } else {
                list = daoUserGroup.findAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }

    public List<TblUserGroup> getUserGroup(String groupId, String username) throws Exception {
        List<TblUserGroup> list = new ArrayList<>();
        try {
            if (ValidationHelper.validateValueNotNull(username) && ValidationHelper.validateValueNotNull(groupId)) {
                list = daoUserGroup.findByGroupIdAndUsername(new Long(groupId), username);
            } else if (ValidationHelper.validateValueNotNull(username)) {
                list = daoUserGroup.findByUsername(username);
            } else if (ValidationHelper.validateValueNotNull(groupId)) {
                list = daoUserGroup.findByGroupId(new Long(groupId));
            } else {
                list = daoUserGroup.findAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }

    public List<JoinUserGroup> queryList(String groupId, String username) throws Exception {
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

    public void save(TblUserGroup userGroup) throws Exception {
        try {
            daoUserGroup.save(userGroup);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public void delete(TblUserGroup userGroup) throws Exception {
        try {
            daoUserGroup.delete(userGroup);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
