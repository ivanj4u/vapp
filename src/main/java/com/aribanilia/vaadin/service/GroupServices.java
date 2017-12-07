package com.aribanilia.vaadin.service;

import com.aribanilia.vaadin.dao.GroupDao;
import com.aribanilia.vaadin.entity.TblGroup;
import com.aribanilia.vaadin.util.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GroupServices extends AuditTrailServices {

    @Autowired private GroupDao daoGroup;

    private static final Logger logger = LoggerFactory.getLogger(GroupServices.class);

    public TblGroup getGroup(String groupId) {
        TblGroup group = null;
        try {
            group = daoGroup.findOne(new Long(groupId));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return group;
    }

    public List<TblGroup> queryList(String groupId) {
        List<TblGroup> list = new ArrayList<>();
        if (ValidationHelper.validateValueNotNull(groupId)) {
            TblGroup group = daoGroup.findOne(new Long(groupId));
            if (group != null)
                list.add(group);
        } else {
            list = daoGroup.findAll();
        }
        return list;
    }

    public List<TblGroup> getAllGroup() {
        List<TblGroup> list = new ArrayList<>();
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
            saveAudit(group);
            daoGroup.save(group);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    public void update(Object pojo) throws Exception {
        TblGroup updatedGroup = (TblGroup) pojo;
        try {
            TblGroup group = daoGroup.findOne(updatedGroup.getGroupId());
            BeanUtils.copyProperties(updatedGroup, group);
            updateAudit(group);
            daoGroup.save(group);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
