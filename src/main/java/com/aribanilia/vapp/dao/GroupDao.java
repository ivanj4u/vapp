/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.dao;

import com.aribanilia.vapp.entity.TblGroup;
import org.springframework.data.repository.CrudRepository;

public interface GroupDao extends CrudRepository<TblGroup, Long> {
}
