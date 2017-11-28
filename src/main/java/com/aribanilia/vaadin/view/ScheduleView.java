/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.view;

import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.framework.impl.AbstractScreen;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;

@UIScope
@SpringView(name = Constants.VIEW_ID.SCHEDULE_VIEW)
public class ScheduleView extends AbstractScreen implements View {
    @Override
    protected void initComponents() {
        setContent(new Label("This is Schedule View"));
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
