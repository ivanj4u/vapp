/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.view;

import com.aribanilia.vapp.framework.AbstractScreen;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;

@UIScope
@SpringView
public class TransactionView extends AbstractScreen implements View {

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
