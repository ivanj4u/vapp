/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.services;

import com.aribanilia.vaadin.dao.ParamDao;
import com.aribanilia.vaadin.entity.TblParam;
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
public class ParamServices extends AuditTrailServices {
    @Autowired private ParamDao daoParam;

    private static final Logger logger = LoggerFactory.getLogger(ParamServices.class);

    public List<TblParam> queryList(String key) throws Exception {
        List<TblParam> list = new ArrayList<>();
        try {
            if (ValidationHelper.validateValueNotNull(key)) {
                list = daoParam.queryTblParamsByKeyLike(("%" + key + "%"));
            } else {
                list = daoParam.findAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }

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

    @Override
    public void save(Object pojo) throws Exception {
        TblParam param = (TblParam) pojo;
        try {
            saveAudit(param);
            daoParam.save(param);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    public void update(Object pojo) throws Exception {
        TblParam updatedParam = (TblParam) pojo;
        try {
            TblParam param = daoParam.findOne(updatedParam.getKey());
            BeanUtils.copyProperties(updatedParam, param);
            updateAudit(param);
            daoParam.save(param);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
