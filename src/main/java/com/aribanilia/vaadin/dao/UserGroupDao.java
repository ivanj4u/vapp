/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.dao;

import com.aribanilia.vaadin.entity.TblUserGroup;
import com.aribanilia.vaadin.entity.TblUserGroupId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserGroupDao extends CrudRepository<TblUserGroup, TblUserGroupId> {

    @Override
    List<TblUserGroup> findAll();

    List<TblUserGroup> findByGroupId(String groupId);

    List<TblUserGroup> findByUsername(String username);

    List<TblUserGroup> findByGroupIdAndUsername(String groupId, String username);

}
