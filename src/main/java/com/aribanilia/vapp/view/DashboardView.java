/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.view;

import com.aribanilia.vapp.framework.AbstractScreen;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;

@SpringView(name = DashboardView.MENU_ID)
public class DashboardView extends AbstractScreen {
    public static final String MENU_ID = "100";

    @Override
    protected void initComponents() {
        setContent(new Label("Dashboard View"));
    }

    @Override
    public void setModeNew() {

    }

    @Override
    public void setModeUpdate() {

    }

    @Override
    public void setModeView() {

    }
}
