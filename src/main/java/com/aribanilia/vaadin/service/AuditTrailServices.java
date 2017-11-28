package com.aribanilia.vaadin.service;

import com.aribanilia.vaadin.entity.AuditTrail;
import com.aribanilia.vaadin.entity.TblUser;
import com.vaadin.server.VaadinSession;

import java.util.Date;

public class AuditTrailServices {

    public void save(Object pojo) throws Exception {
        TblUser user = VaadinSession.getCurrent().getAttribute(TblUser.class);
        Date now = new Date();
        if (user != null) {
            ((AuditTrail) pojo).setCreateBy(user.getUsername());
            ((AuditTrail) pojo).setCreateDate(now);
            ((AuditTrail) pojo).setVersi(now.getTime());
        }
    }

    public void update(Object pojo) throws Exception {
        TblUser user = VaadinSession.getCurrent().getAttribute(TblUser.class);
        Date now = new Date();
        if (user != null) {
            ((AuditTrail) pojo).setUpdateBy(user.getUsername());
            ((AuditTrail) pojo).setUpdateDate(now);
            ((AuditTrail) pojo).setVersi(now.getTime());
        }
    }

}
