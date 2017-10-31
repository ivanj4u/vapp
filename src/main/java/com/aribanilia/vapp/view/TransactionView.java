/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.view;

import com.aribanilia.vapp.framework.AbstractScreen;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;

@SpringView(name = TransactionView.MENU_ID)
public class TransactionView extends AbstractScreen {
    public static final String MENU_ID = "500";

    @Override
    protected void initComponents() {
        setContent(new Label("Transaction View"));
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
