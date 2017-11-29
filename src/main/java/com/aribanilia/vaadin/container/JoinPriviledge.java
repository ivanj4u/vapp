/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.container;

import com.aribanilia.vaadin.entity.TblGroup;
import com.aribanilia.vaadin.entity.TblMenu;
import com.aribanilia.vaadin.entity.TblPriviledge;

public class JoinPriviledge {
    // Join TblPriviledge
    private TblPriviledge priviledge;
    private long priviledge_groupId;
    private String priviledge_menuId;
    // Join TblGroup
    private TblGroup group;
    private long group_groupId;
    private String group_groupName;
    // Join TblMenu
    private TblMenu menu;
    private String menu_menuId;
    private String menu_menuName;
    private String menu_menuClass;

    public JoinPriviledge(TblPriviledge priviledge, TblGroup group, TblMenu menu) {
        // Set TblPriviledge
        this.priviledge = priviledge;
        setPriviledge_groupId(priviledge.getGroupId());
        setPriviledge_menuId(priviledge.getMenuId());
        // Set TblGroup
        this.group = group;
        setGroup_groupId(group.getGroupId());
        setGroup_groupName(group.getGroupName());
        // Set TblMenu
        this.menu = menu;
        setMenu_menuId(menu.getMenuId());
        setMenu_menuName(menu.getMenuName());
        setMenu_menuClass(menu.getMenuClass());
    }

    public long getPriviledge_groupId() {
        return priviledge_groupId;
    }

    public void setPriviledge_groupId(long priviledge_groupId) {
        this.priviledge_groupId = priviledge_groupId;
    }

    public String getPriviledge_menuId() {
        return priviledge_menuId;
    }

    public void setPriviledge_menuId(String priviledge_menuId) {
        this.priviledge_menuId = priviledge_menuId;
    }

    public long getGroup_groupId() {
        return group_groupId;
    }

    public void setGroup_groupId(long group_groupId) {
        this.group_groupId = group_groupId;
    }

    public String getGroup_groupName() {
        return group_groupName;
    }

    public void setGroup_groupName(String group_groupName) {
        this.group_groupName = group_groupName;
    }

    public String getMenu_menuId() {
        return menu_menuId;
    }

    public void setMenu_menuId(String menu_menuId) {
        this.menu_menuId = menu_menuId;
    }

    public String getMenu_menuName() {
        return menu_menuName;
    }

    public void setMenu_menuName(String menu_menuName) {
        this.menu_menuName = menu_menuName;
    }

    public String getMenu_menuClass() {
        return menu_menuClass;
    }

    public void setMenu_menuClass(String menu_menuClass) {
        this.menu_menuClass = menu_menuClass;
    }
}
