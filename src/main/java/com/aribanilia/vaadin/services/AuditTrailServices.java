package com.aribanilia.vaadin.services;

import com.aribanilia.vaadin.entity.AuditTrail;
import com.aribanilia.vaadin.entity.TblUser;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.vaadin.server.VaadinSession;

import java.util.Date;

public abstract class AuditTrailServices {

    protected void saveAudit(Object pojo) throws Exception {
        TblUser user = VaadinSession.getCurrent().getAttribute(TblUser.class);
        Date now = new Date();
        if (user != null) {
            ((AuditTrail) pojo).setCreateBy(user.getUsername());
        } else {
            ((AuditTrail) pojo).setCreateBy(Constants.APP_USER.SYS_USER);
        }
        ((AuditTrail) pojo).setCreateDate(now);
        ((AuditTrail) pojo).setVersi(now.getTime());
    }

    protected void updateAudit(Object pojo) throws Exception {
        TblUser user = VaadinSession.getCurrent().getAttribute(TblUser.class);
        Date now = new Date();
        if (user != null) {
            ((AuditTrail) pojo).setUpdateBy(user.getUsername());
        } else {
            ((AuditTrail) pojo).setUpdateBy(Constants.APP_USER.SYS_USER);
        }
        ((AuditTrail) pojo).setUpdateDate(now);
        ((AuditTrail) pojo).setVersi(now.getTime());
    }

    public abstract void save(Object pojo) throws Exception;
    public abstract void update(Object pojo) throws Exception;
}
