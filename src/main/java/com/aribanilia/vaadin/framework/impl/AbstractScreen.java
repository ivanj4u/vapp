/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.framework.impl;

import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.loader.MenuLoader;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Panel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@UIScope
public abstract class AbstractScreen extends Panel implements PriviledgeModel {

    @Autowired private MenuLoader menuLoader;

    protected boolean isInit = false;
    protected int mode = -1;
    protected String param;

    public AbstractScreen() {
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @PostConstruct
    public void show() {
        if (isInit) {
            onShow();
            return;
        }
        isInit = true;
        setContent(null);
        setWidth(100, Unit.PERCENTAGE);
        setSizeFull();
        beforeInitComponent();
        initComponents();
        afterInitComponent();
        onShow();
        Responsive.makeResponsive(this);
    }

    protected void beforeInitComponent() {

    }

    protected void afterInitComponent() {

    }

    protected void onShow() {

    }

    protected abstract void initComponents();

    public boolean setMode(int mode) {
        this.mode = mode;
        if (mode == Constants.APP_MODE.MODE_NEW) {
            if (isAuthorizedToAdd()) {
                setModeNew();
                return true;
            }
        } else if (mode == Constants.APP_MODE.MODE_UPDATE) {
            if (isAuthorizedToUpdate()) {
                setModeUpdate();
                return true;
            }
        } else if (mode == Constants.APP_MODE.MODE_VIEW) {
            if (isAuthorizedToView()) {
                setModeView();
                return true;
            }
        }
        return false;
    }

    public int getMode() {
        return this.mode;
    }

    public abstract void setModeNew();

    public abstract void setModeUpdate();

    public abstract void setModeView();

    @Override
    public boolean isAuthorizedToView() {
        return menuLoader.getPriviledge(getClass().getName()) == null
                || (menuLoader.getPriviledge(getClass().getName()) != null && menuLoader.getPriviledge(getClass().getName())
                .getIsView() == '1');
    }

    @Override
    public boolean isAuthorizedToUpdate() {
        return menuLoader.getPriviledge(getClass().getName()) == null
                || (menuLoader.getPriviledge(getClass().getName()) != null && menuLoader.getPriviledge(getClass().getName())
                .getIsUpdate() == '1');
    }

    @Override
    public boolean isAuthorizedToDelete() {
        return menuLoader.getPriviledge(getClass().getName()) == null
                || (menuLoader.getPriviledge(getClass().getName()) != null && menuLoader.getPriviledge(getClass().getName())
                .getIsDelete() == '1');
    }

    @Override
    public boolean isAuthorizedToAdd() {
        return menuLoader.getPriviledge(getClass().getName()) == null
                || (menuLoader.getPriviledge(getClass().getName()) != null && menuLoader.getPriviledge(getClass().getName())
                .getIsAdd() == '1');
    }
}
