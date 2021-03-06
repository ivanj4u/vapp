/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.services;

import com.aribanilia.vaadin.dao.PriviledgeDao;
import com.aribanilia.vaadin.entity.TblPriviledge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PriviledgeServices {
    @Autowired private PriviledgeDao daoPriviledge;

    private static Logger logger = LoggerFactory.getLogger(PriviledgeServices.class);

    public List<TblPriviledge> getGroupPriviledge(String groupId) throws Exception {
        List<TblPriviledge> list = new ArrayList<>();
        try {
            list = daoPriviledge.findByGroupId(new Long(groupId));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }

    public TblPriviledge getMenuPriviledge(String menuId, Long groupId) throws Exception {
        TblPriviledge priviledge = null;
        try {
            priviledge = daoPriviledge.findByMenuIdAndGroupId(menuId, groupId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return priviledge;
    }

    public void save(TblPriviledge priviledge) throws Exception {
        try {
            daoPriviledge.save(priviledge);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public void deletePriviledgeGroup(Long groupId) throws Exception {
        try {
            List<TblPriviledge> list = daoPriviledge.findByGroupId(groupId);
            for (TblPriviledge priviledge : list) {
                delete(priviledge);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public void delete(TblPriviledge priviledge) throws Exception {
        try {
            daoPriviledge.delete(priviledge);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
