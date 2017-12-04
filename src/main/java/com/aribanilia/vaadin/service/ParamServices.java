/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.service;

import com.aribanilia.vaadin.dao.ParamDao;
import com.aribanilia.vaadin.entity.TblParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ParamServices {
    @Autowired private ParamDao daoParam;

    private static final Logger logger = LoggerFactory.getLogger(ParamServices.class);

    public List<TblParam> getList() throws Exception {
        List<TblParam> list = new ArrayList<>();
        try {
            list = daoParam.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }

    public TblParam getParam(String key) throws Exception {
        TblParam param = null;
        try {
            param = daoParam.findOne(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return param;
    }

}
