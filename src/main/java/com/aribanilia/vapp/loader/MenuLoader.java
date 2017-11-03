/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.loader;

import com.aribanilia.vapp.entity.TblMenu;
import com.aribanilia.vapp.entity.TblPriviledge;
import com.aribanilia.vapp.entity.TblUser;
import com.aribanilia.vapp.entity.TblUserGroup;
import com.aribanilia.vapp.framework.AbstractScreen;
import com.aribanilia.vapp.service.MenuServices;
import com.aribanilia.vapp.service.PriviledgeServices;
import com.aribanilia.vapp.service.UserGroupServices;
import com.aribanilia.vapp.service.UserServices;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SpringComponent
public class MenuLoader {
    @Autowired private UserServices servicesUser;
    @Autowired private UserGroupServices servicesUserGroup;
    @Autowired private PriviledgeServices servicesPriviledge;
    @Autowired private MenuServices servicesMenu;

    private static Vector<TblMenu> v = new Vector<>();
    private Vector<TblMenu> vSessionedPerUser = new Vector<>();
    private ConcurrentHashMap<String, AbstractScreen> cacheClass = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Class<? extends View>> cacheView = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(MenuLoader.class);

    @PostConstruct
    public void load() {
        List<TblMenu> list = servicesMenu.getAllMenu();
        v.clear();
        v.addAll(list);
    }

    public AbstractScreen getScreen(String menuId) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        for (TblMenu menu : v) {
            if (menu.getMenuId().equals(menuId)) {
                AbstractScreen obj = cacheClass.get(menu.getMenuId());
                if (obj != null)
                    return obj;
                obj = (AbstractScreen) Class.forName(menu.getMenuClass()).newInstance();
                cacheClass.put(menu.getMenuId(), obj);
                obj.setParam(menu.getParam());
                return obj;
            }
        }
        return null;
    }

    public void resetScreen(String className) {
        List<String> listOfRemoved = new ArrayList<>();
        for (Iterator<String> it = cacheClass.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            AbstractScreen screen = cacheClass.get(key);
            if (screen.getClass().getName().equals(className)) {
                listOfRemoved.add(key);
            }
        }
        for (String key : listOfRemoved)
            cacheClass.remove(key);
    }

    private Hashtable<String, TblPriviledge> hSessionedMenuperUser = new Hashtable<>();
    private Hashtable<String, TblPriviledge> hSessionedMenuperUser2 = new Hashtable<>();

    public void setAuthorizedMenu(TblUser user) {
        hSessionedMenuperUser.clear();
        vSessionedPerUser.removeAllElements();
        vSessionedPerUser.addAll(v);
        Vector<TblMenu> vTemp = new Vector<>();
        try {

            List<TblUserGroup> userGroups = servicesUserGroup.getUserGroup(user.getUsername());
            for (TblUserGroup userGroup : userGroups) {
                List<TblPriviledge> priviledges = servicesPriviledge.getGroupPriviledge(userGroup.getGroupId());
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
                        if (menu != null && menu.getMenuClass() != null) {

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
        cacheClass.clear();
    }
}
