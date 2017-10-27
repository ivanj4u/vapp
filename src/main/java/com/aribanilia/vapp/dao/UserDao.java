/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.dao;

import com.aribanilia.vapp.entity.TblUser;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<TblUser, String> {

    TblUser findByUsernameAndPassword(String username, String password);
}
