/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.framework.impl;

import com.aribanilia.vaadin.framework.listener.DetailCallbackListener;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
public abstract class AbstractDetailScreen extends AbstractScreen {

    protected DetailCallbackListener listener;
    protected Button btnSave, btnCancel;
    protected HorizontalLayout buttonBar;

    public AbstractDetailScreen(DetailCallbackListener listener) {
        this.listener = listener;
        this.addStyleName(ValoTheme.PANEL_WELL);
    }

    public AbstractDetailScreen() {
        this.addStyleName(ValoTheme.PANEL_WELL);
    }

    public void setListener(DetailCallbackListener listener) {
        this.listener = listener;
    }

    @Override
    protected void initComponents() {
        VerticalLayout layout = new VerticalLayout();

        initButton();
        layout.addComponent(initDetail());
        layout.addComponent(buttonBar);
        layout.setComponentAlignment(buttonBar, Alignment.MIDDLE_CENTER);

        setContent(layout);
    }

    protected void initButton() {
        buttonBar = new HorizontalLayout();
        buttonBar.setSpacing(true);
        buttonBar.addComponent(btnSave = new Button("Simpan"));
        buttonBar.addComponent(btnCancel = new Button("Batal"));

        btnSave.addListener(event -> doSave());

        btnCancel.addListener(event -> doCancel());
    }

    protected void doCancel() {
        if (listener != null)
            listener.onCancel();
    }

    protected abstract Component initDetail();
    protected abstract void doSave();
    protected abstract boolean doValidate();
    protected abstract void doReset();
    protected abstract void setContentById(Object pojo);

}
