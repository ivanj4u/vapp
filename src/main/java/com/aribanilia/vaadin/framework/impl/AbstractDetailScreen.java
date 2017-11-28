/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.framework.impl;

import com.aribanilia.vaadin.framework.listener.DetailCallbackListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
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

    protected void initButton() {
        buttonBar = new HorizontalLayout();
        buttonBar.setSpacing(true);
        buttonBar.addComponent(btnSave = new Button("Simpan"));
        buttonBar.addComponent(btnCancel = new Button("Batal"));

        btnSave.addListener(event -> {
            doSave();
        });

        btnCancel.addListener(event -> {
            doCancel();
        });
    }

    protected void doCancel() {
        if (listener != null)
            listener.onCancel();
    }

    protected abstract void doSave();
    protected abstract void doReset();
    protected abstract void setContentById(Object pojo);

}
