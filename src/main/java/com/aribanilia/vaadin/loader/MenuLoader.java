/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.loader;

import com.aribanilia.vaadin.entity.TblMenu;
import com.aribanilia.vaadin.entity.TblPriviledge;
import com.aribanilia.vaadin.entity.TblUser;
import com.aribanilia.vaadin.entity.TblUserGroup;
import com.aribanilia.vaadin.framework.impl.AbstractScreen;
import com.aribanilia.vaadin.services.MenuServices;
import com.aribanilia.vaadin.services.PriviledgeServices;
import com.aribanilia.vaadin.services.UserGroupServices;
import com.vaadin.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

@SpringComponent
public class MenuLoader {
    @Autowired private UserGroupServices servicesUserGroup;
    @Autowired private PriviledgeServices servicesPriviledge;
    @Autowired private MenuServices servicesMenu;
    @Autowired ApplicationContext applicationContext;

    private Vector<TblMenu> v = new Vector<>();
    private Vector<TblMenu> vSessionedPerUser = new Vector<>();

    private static final Logger logger = LoggerFactory.getLogger(MenuLoader.class);

    @PostConstruct
    public void load() {
        List<TblMenu> list = servicesMenu.getAllMenu();
        v.clear();
        v.addAll(list);
    }

    @SuppressWarnings("unchecked")
    public AbstractScreen getScreen(String menuId) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        for (TblMenu menu : v) {
            if (menu.getMenuId().equals(menuId)) {
                Class c = Class.forName(menu.getMenuClass());
                AbstractScreen obj = (AbstractScreen) applicationContext.getBean(c);
                obj.setParam(menu.getParam());
                return obj;
            }
        }
        return null;
    }

    private Hashtable<String, TblPriviledge> hSessionedMenuperUser = new Hashtable<>();
    private Hashtable<String, TblPriviledge> hSessionedMenuperUser2 = new Hashtable<>();

    public void setAuthorizedMenu(TblUser user) {
        hSessionedMenuperUser.clear();
        vSessionedPerUser.removeAllElements();
        vSessionedPerUser.addAll(v);
        Vector<TblMenu> vTemp = new Vector<>();
        try {

            List<TblUserGroup> userGroups = servicesUserGroup.getUserGroupByUsername(user.getUsername());
            for (TblUserGroup userGroup : userGroups) {
                List<TblPriviledge> priviledges = servicesPriviledge.getGroupPriviledge(String.valueOf(userGroup.getGroupId()));
                for (TblPriviledge p : priviledges) {
                    TblPriviledge privPrev = hSessionedMenuperUser.get(p.getMenuId());
                    if (privPrev != null) {
                        if (privPrev.getIsAdd() == '0')
                            privPrev.setIsAdd(p.getIsAdd());
                        if (privPrev.getIsDelete() == '0')
                            privPrev.setIsDelete(p.getIsDelete());
                        if (privPrev.getIsUpdate() == '0')
                            privPrev.setIsUpdate(p.getIsUpdate());
                        if (privPrev.getIsView() == '0')
                            privPrev.setIsView(p.getIsView());
                    } else {
                        hSessionedMenuperUser.put(p.getMenuId(), p);
                        TblMenu menu = servicesMenu.getMenu(p.getMenuId());
                        if (menu != null && menu.getMenuClass() != null && !menu.getMenuClass().equals("")) {
                            hSessionedMenuperUser2.put(menu.getMenuClass(), p);
                        }
                        vTemp.add(menu);
                    }
                }
            }
            for (TblMenu menu : v) {
                boolean isShowed = false;
                for (TblMenu menu_ : vTemp) {
                    if (menu_.getMenuId().equals(menu.getMenuId())) {
                        isShowed = true;
                        break;
                    }
                }
                if (!isShowed) {
                    vSessionedPerUser.remove(menu);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public TblPriviledge getPriviledge(String screenClassName) {
        return hSessionedMenuperUser2.get(screenClassName);
    }

    public Vector<TblMenu> getAuthorizedMenu() {
        return vSessionedPerUser;
    }

    public void clear() {
        hSessionedMenuperUser.clear();
        hSessionedMenuperUser2.clear();
        vSessionedPerUser.removeAllElements();
    }
}
