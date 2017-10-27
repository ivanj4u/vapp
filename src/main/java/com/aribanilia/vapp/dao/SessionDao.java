/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.dao;

import com.aribanilia.vapp.entity.TblSession;
import org.springframework.data.repository.CrudRepository;

public interface SessionDao extends CrudRepository<TblSession, String> {
}
