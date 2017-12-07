/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.service;

import com.aribanilia.vaadin.dao.MenuDao;
import com.aribanilia.vaadin.entity.TblMenu;
import com.aribanilia.vaadin.util.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MenuServices extends AuditTrailServices {
    @Autowired  private MenuDao daoMenu;
    private static final Logger logger = LoggerFactory.getLogger(MenuServices.class);

    public List<TblMenu> queryList(String menuId, String menuName, String parentId) {
        List<TblMenu> list = new ArrayList<>();
        try {
            if (ValidationHelper.validateValueNotNull(menuId) && ValidationHelper.validateValueNotNull(menuName)
                    && ValidationHelper.validateValueNotNull(parentId)) {
                list = daoMenu.queryTblMenusByMenuIdEqualsAndMenuNameLikeAndParentIdEquals(menuId, ("%" + menuName + "%"), parentId);
            } else if (ValidationHelper.validateValueNotNull(menuId) && ValidationHelper.validateValueNotNull(menuName)) {
                list = daoMenu.queryTblMenusByMenuIdEqualsAndMenuNameLike(menuId, ("%" + menuName + "%"));
            } else if (ValidationHelper.validateValueNotNull(menuId) && ValidationHelper.validateValueNotNull(parentId)) {
                list = daoMenu.queryTblMenusByMenuIdEqualsAndParentIdEquals(menuId, parentId);
            } else if (ValidationHelper.validateValueNotNull(menuName) && ValidationHelper.validateValueNotNull(parentId)) {
                list = daoMenu.queryTblMenusByMenuNameLikeAndParentIdEquals(("%" + menuName + "%"), parentId);
            } else if (ValidationHelper.validateValueNotNull(menuId)) {
                TblMenu menu = daoMenu.findOne(menuId);
                if (menu != null)
                    list.add(menu);
            } else if (ValidationHelper.validateValueNotNull(menuName)) {
                list = daoMenu.queryTblMenusByMenuNameLike(("%" + menuName + "%"));
            } else if (ValidationHelper.validateValueNotNull(parentId)) {
                list = daoMenu.queryTblMenusByParentIdEquals(parentId);
            } else {
                list = daoMenu.findAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }

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


    @Override
    public void save(Object pojo) throws Exception {
        TblMenu menu = (TblMenu) pojo;
        try {
            saveAudit(menu);
            daoMenu.save(menu);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    public void update(Object pojo) throws Exception {
        TblMenu updatedMenu = (TblMenu) pojo;
        try {
            TblMenu menu = daoMenu.findOne(updatedMenu.getMenuId());
            BeanUtils.copyProperties(updatedMenu, menu);
            updateAudit(menu);
            daoMenu.save(menu);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
