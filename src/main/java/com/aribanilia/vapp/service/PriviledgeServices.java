/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.service;

import com.aribanilia.vapp.dao.PriviledgeDao;
import com.aribanilia.vapp.entity.TblPriviledge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PriviledgeServices {
    @Autowired private PriviledgeDao daoPriviledge;

    private static Logger logger = LoggerFactory.getLogger(PriviledgeServices.class);

    public List<TblPriviledge> getGroupPriviledge(Long groupId) throws Exception {
        logger.info("Start Services getGroupPriviledge : " + groupId);
        List<TblPriviledge> list = null;
        try {
            list = daoPriviledge.findByGroupId(groupId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End Services getGroupPriviledge : " + groupId);
        return list;
    }
}
