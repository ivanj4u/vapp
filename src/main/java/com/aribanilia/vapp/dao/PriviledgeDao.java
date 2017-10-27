/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.dao;

import com.aribanilia.vapp.entity.TblPriviledge;
import com.aribanilia.vapp.entity.TblPriviledgeId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PriviledgeDao extends CrudRepository<TblPriviledge, TblPriviledgeId> {

    List<TblPriviledge> findByGroupId(Long groupId);

}
