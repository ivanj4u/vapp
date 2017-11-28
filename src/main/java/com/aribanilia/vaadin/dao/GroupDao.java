/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.dao;

import com.aribanilia.vaadin.entity.TblGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupDao extends CrudRepository<TblGroup, Long> {

    @Override
    List<TblGroup> findAll();

}
