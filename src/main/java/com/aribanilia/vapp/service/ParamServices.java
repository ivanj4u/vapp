/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.service;

import com.aribanilia.vapp.dao.ParamDao;
import com.aribanilia.vapp.entity.TblParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ParamServices {
    @Autowired private ParamDao daoParam;

    private static final Logger logger = LoggerFactory.getLogger(ParamServices.class);

    public List<TblParam> getList() throws Exception {
        logger.info("Start Services getList ");
        List<TblParam> list = null;
        try {
            list = daoParam.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End Services getList ");
        return list;
    }

    public TblParam getParam(String key) throws Exception {
        logger.info("Start Services getParam : " + key);
        TblParam param = null;
        try {
            param = daoParam.findOne(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("End Services getParam : " + key);
        return param;
    }

}
