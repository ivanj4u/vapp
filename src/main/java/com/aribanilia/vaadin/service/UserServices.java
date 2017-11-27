/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.service;

import com.aribanilia.vaadin.dao.UserDao;
import com.aribanilia.vaadin.entity.TblUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServices {
    @Autowired private UserDao daoUser;

    private static final Logger logger = LoggerFactory.getLogger(UserServices.class);

    public List<TblUser> queryList(String username, String name) throws Exception {
        List<TblUser> list = null;
        try {
            list = daoUser.queryTblUsersByUsernameEqualsAndNameIsLike(username, name);
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

    public void save(TblUser user) throws Exception {
        try {
            daoUser.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

}
