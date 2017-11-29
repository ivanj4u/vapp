package com.aribanilia.vaadin.service;

import com.aribanilia.vaadin.dao.GroupDao;
import com.aribanilia.vaadin.entity.TblGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GroupServices extends AuditTrailServices {

    @Autowired private GroupDao daoGroup;

    private static final Logger logger = LoggerFactory.getLogger(GroupServices.class);

    public List<TblGroup> queryList() {
        List<TblGroup> list = null;
        try {
            list = daoGroup.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }

    @Override
    public void save(Object pojo) throws Exception {
        TblGroup group = (TblGroup) pojo;
        try {
            super.save(group);
            daoGroup.save(group);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    public void update(Object pojo) throws Exception {
        TblGroup group = (TblGroup) pojo;
        try {
            TblGroup updatePojo = daoGroup.findOne(group.getGroupId());
            updatePojo.setGroupName(group.getGroupName());
            // Update Pojo AuditTrail
            super.update(updatePojo);
            daoGroup.save(updatePojo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
