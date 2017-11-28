/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.dao;

import com.aribanilia.vaadin.entity.TblUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDao extends CrudRepository<TblUser, String> {

    TblUser findByUsernameAndPassword(String username, String password);

    @Override
    List<TblUser> findAll();

    List<TblUser> queryTblUsersByNameLike(String name);

    List<TblUser> queryTblUsersByUsernameEquals(String username);

    List<TblUser> queryTblUsersByUsernameEqualsAndNameIsLike(String username, String name);

}
