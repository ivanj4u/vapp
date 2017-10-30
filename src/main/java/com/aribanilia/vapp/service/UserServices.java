/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.service;

import com.aribanilia.vapp.dao.UserDao;
import com.aribanilia.vapp.entity.TblUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServices {
    @Autowired private UserDao daoUser;

    private static final Logger logger = LoggerFactory.getLogger(UserServices.class);

    public TblUser getUser(String username) throws Exception {
        logger.info("Start Services getUser : " + username);
        TblUser user = null;
        try {
            user = daoUser.findOne(username);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End Services getUser : " + username);
        return user;
    }

    public void save(TblUser user) throws Exception {
        logger.info("Start Services save : " + user.getUsername());
        try {
            daoUser.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End Services save : " + user.getUsername());
    }

}
