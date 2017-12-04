/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.container;

import com.vaadin.ui.CheckBox;

public class TreeMenuContainer {
    private String menuId;
    private String menuName;
    private String isAdd;
    private String isUpdate;
    private String isView;
    private String isDelete;

    private TreeMenuContainer parentMenu;

    public TreeMenuContainer() {
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(String isAdd) {
        this.isAdd = isAdd;
    }

    public String getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getIsView() {
        return isView;
    }

    public void setIsView(String isView) {
        this.isView = isView;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public TreeMenuContainer getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(TreeMenuContainer parentMenu) {
        this.parentMenu = parentMenu;
    }
}
