/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.view;

import com.aribanilia.vapp.framework.AbstractScreen;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;

@SpringView(name = ReportsView.MENU_ID)
public class ReportsView extends AbstractScreen {
    public static final String MENU_ID = "200";

    @Override
    protected void initComponents() {
        setContent(new Label("Report View"));
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
