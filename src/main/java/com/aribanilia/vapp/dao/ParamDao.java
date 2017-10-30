/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.dao;

import com.aribanilia.vapp.entity.TblParam;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParamDao extends CrudRepository<TblParam, String> {

    @Override
    List<TblParam> findAll();
}
