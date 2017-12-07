/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.dao;

import com.aribanilia.vaadin.entity.TblMenu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuDao extends CrudRepository<TblMenu, String> {

    List<TblMenu> queryTblMenusByMenuIdEqualsAndMenuNameLikeAndParentIdEquals(String menuId, String menuName, String parentId);

    List<TblMenu> queryTblMenusByMenuIdEqualsAndMenuNameLike(String menuId, String menuName);

    List<TblMenu> queryTblMenusByMenuNameLikeAndParentIdEquals(String menuName, String parentId);

    List<TblMenu> queryTblMenusByMenuIdEqualsAndParentIdEquals(String menuId, String parentId);

    List<TblMenu> queryTblMenusByMenuNameLike(String menuName);

    List<TblMenu> queryTblMenusByParentIdEquals(String parentId);

    @Override
    @Query(value = "select menu from TblMenu menu order by menu.parentId asc , menu.position asc")
    List<TblMenu> findAll();

}
