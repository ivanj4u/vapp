/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.dao;

import com.aribanilia.vapp.entity.TblUserGroup;
import com.aribanilia.vapp.entity.TblUserGroupId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserGroupDao extends CrudRepository<TblUserGroup, TblUserGroupId> {

    List<TblUserGroup> findByUsername(String username);
}
