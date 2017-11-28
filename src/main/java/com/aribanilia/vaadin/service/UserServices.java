/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.service;

import com.aribanilia.vaadin.dao.UserDao;
import com.aribanilia.vaadin.entity.TblUser;
import com.aribanilia.vaadin.util.ValidationHelper;
import com.vaadin.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServices {
    @Autowired private UserDao daoUser;

    private static final Logger logger = LoggerFactory.getLogger(UserServices.class);

    public List<TblUser> queryList(String username, String name) throws Exception {
        List<TblUser> list = null;
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

    public void save(TblUser user) throws Exception {
        try {
            TblUser createBy = VaadinSession.getCurrent().getAttribute(TblUser.class);
            if (createBy != null) {
                user.setCreateBy(createBy.getUsername());
                user.setCreateDate(new Date());
                user.setVersi(user.getCreateDate().getTime());
            }
            daoUser.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public void update(TblUser user) throws Exception {
        try {
            TblUser tblUser = daoUser.findOne(user.getUsername());
            tblUser.setName(user.getName());
            tblUser.setStatus(user.getStatus());
            tblUser.setLoginFailCount(user.getLoginFailCount());
            tblUser.setPhone(user.getPhone());
            tblUser.setEmail(user.getEmail());
            tblUser.setStartTime(user.getStartTime());
            tblUser.setEndTime(user.getEndTime());

            TblUser updateBy = VaadinSession.getCurrent().getAttribute(TblUser.class);
            if (updateBy != null) {
                tblUser.setUpdateBy(updateBy.getUsername());
                tblUser.setUpdateDate(new Date());
                tblUser.setVersi(user.getUpdateDate().getTime());
            }

            daoUser.save(tblUser);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

}
