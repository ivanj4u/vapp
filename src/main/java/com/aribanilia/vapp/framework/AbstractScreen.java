/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.framework;

import com.aribanilia.vapp.loader.MenuLoader;
import com.aribanilia.vapp.model.PriviledgeModel;
import com.vaadin.navigator.View;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Panel;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringView
public abstract class AbstractScreen extends Panel implements PriviledgeModel, View {

    @Autowired private MenuLoader menuLoader;

    protected boolean isInit = false;
    protected int mode = -1;
    protected Object pojo;
    protected String param;

    public AbstractScreen() {
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

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
        if (mode == MODE_NEW) {
            if (isAuthorizedToAdd()) {
                setModeNew();
                return true;
            }
        } else if (mode == MODE_UPDATE) {
            if (isAuthorizedToUpdate()) {
                setModeUpdate();
                return true;
            }
        } else if (mode == MODE_VIEW) {
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
        return menuLoader.getPriviledge(getClass().getName()) != null
                ? menuLoader.getPriviledge(getClass().getName()).getIsView() == '1' : false;
    }

    @Override
    public boolean isAuthorizedToUpdate() {
        boolean b = false;
        if (menuLoader.getPriviledge(getClass().getName()) != null) {
            b = menuLoader.getPriviledge(getClass().getName()).getIsUpdate() == '1';
        }
        return b;
    }

    @Override
    public boolean isAuthorizedToDelete() {
        return menuLoader.getPriviledge(getClass().getName()) != null
                ? menuLoader.getPriviledge(getClass().getName()).getIsDelete() == '1' : false;
    }

    @Override
    public boolean isAuthorizedToAdd() {
        return menuLoader.getPriviledge(getClass().getName()) == null
                || (menuLoader.getPriviledge(getClass().getName()) != null && menuLoader.getPriviledge(getClass().getName())
                .getIsAdd() == '1');
    }
}
