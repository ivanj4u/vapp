/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.view;

import com.aribanilia.vapp.framework.AbstractScreen;
import com.vaadin.ui.Label;

public class LandingView extends AbstractScreen {

    @Override
    protected void initComponents() {
        setContent(new Label("This is Landing View"));
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