/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.service;

import com.aribanilia.vaadin.dao.MenuDao;
import com.aribanilia.vaadin.entity.TblMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MenuServices {
    @Autowired  private MenuDao daoMenu;
    private static final Logger logger = LoggerFactory.getLogger(MenuServices.class);

    public TblMenu getMenu(String menuId) {
        TblMenu menu = null;
        try {
            menu = daoMenu.findOne(menuId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return menu;
    }

    public List<TblMenu> getAllMenu() {
        List<TblMenu> list = new ArrayList<>();
        try {
            list = daoMenu.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }
}
