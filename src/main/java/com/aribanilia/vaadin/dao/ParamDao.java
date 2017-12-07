/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.dao;

import com.aribanilia.vaadin.entity.TblParam;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParamDao extends CrudRepository<TblParam, String> {

    List<TblParam> queryTblParamsByKeyLike(String key);

    @Override
    List<TblParam> findAll();

}
