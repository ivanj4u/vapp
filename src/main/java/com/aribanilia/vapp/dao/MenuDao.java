/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.dao;

import com.aribanilia.vapp.entity.TblMenu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuDao extends CrudRepository<TblMenu, String> {

    @Override
    @Query(value = "select menu from TblMenu menu order by menu.parentId asc , menu.position asc")
    List<TblMenu> findAll();
}
