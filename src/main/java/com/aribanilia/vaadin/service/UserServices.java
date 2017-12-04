/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.service;

import com.aribanilia.vaadin.dao.UserDao;
import com.aribanilia.vaadin.entity.TblUser;
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
public class UserServices extends AuditTrailServices {
    @Autowired private UserDao daoUser;

    private static final Logger logger = LoggerFactory.getLogger(UserServices.class);

    @Override
    public void save(Object pojo) throws Exception {
        TblUser createdUser = (TblUser) pojo;
        try {
            super.save(createdUser);
            daoUser.save(createdUser);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    public void update(Object pojo) throws Exception {
        TblUser updatedUser = (TblUser) pojo;
        try {
            TblUser tblUser = daoUser.findOne(updatedUser.getUsername());
            tblUser.setName(updatedUser.getName());
            tblUser.setPassword(updatedUser.getPassword());
            tblUser.setEmail(updatedUser.getEmail());
            tblUser.setPhone(updatedUser.getPhone());
            tblUser.setStatus(updatedUser.getStatus());
            tblUser.setLoginFailCount(updatedUser.getLoginFailCount());
            tblUser.setStartTime(updatedUser.getStartTime());
            tblUser.setEndTime(updatedUser.getEndTime());

            super.update(updatedUser);
            daoUser.save(tblUser);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public List<TblUser> queryList(String username, String name) throws Exception {
        List<TblUser> list = new ArrayList<>();
        try {
            if (ValidationHelper.validateValueNotNull(username) && ValidationHelper.validateValueNotNull(name)) {
                list = daoUser.queryTblUsersByUsernameEqualsAndNameIsLike(username, ("%" + name + "%"));
            } else if (ValidationHelper.validateValueNotNull(username)) {
                list = daoUser.queryTblUsersByUsernameEquals(username);
            } else if (ValidationHelper.validateValueNotNull(name)) {
                list = daoUser.queryTblUsersByNameLike(("%" + name + "%"));
            } else {
                list = daoUser.findAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }

    public TblUser getUser(String username) throws Exception {
        TblUser user = null;
        try {
            user = daoUser.findOne(username);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return user;
    }

}
