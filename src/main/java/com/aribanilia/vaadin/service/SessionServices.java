/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.service;

import com.aribanilia.vaadin.dao.SessionDao;
import com.aribanilia.vaadin.entity.TblSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class SessionServices {

    @Autowired private SessionDao daoSession;

    private static final Logger logger = LoggerFactory.getLogger(SessionServices.class);

    public void sessionLogin(String username, String sessionId, String ip) throws Exception {
        try {
            TblSession pojo = daoSession.findOne(username);
            if (pojo == null) {
                pojo = new TblSession();
                pojo.setUsername(username);
                // Audit Trail
                pojo.setCreateBy(username);
                pojo.setCreateDate(new Date());
            } else {
                pojo.setUpdateBy(username);
                pojo.setUpdateDate(new Date());
            }
            pojo.setSessionId(sessionId);
            pojo.setIp(ip);
            // Save or Update TblSession
            daoSession.save(pojo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public boolean sessionCheck(String username, String sessionId) throws Exception {
        boolean valid = true;
        try {
            TblSession pojo = daoSession.findOne(username);
            if (pojo == null) {
                valid = false;
            } else {
                if (!pojo.getSessionId().equals(sessionId))
                    valid = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return valid;
    }
}
