/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.framework;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
public abstract class AbstractSearchScreen extends AbstractScreen {

    @Override
    protected void initComponents() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(false);
        layout.setStyleName(ValoTheme.PANEL_BORDERLESS);
        Label lblTitle = new Label();
        lblTitle.setValue(getTitle());
        lblTitle.setStyleName(ValoTheme.LABEL_H4);
        lblTitle.setStyleName(ValoTheme.LABEL_COLORED);
        layout.addComponent(lblTitle);

        Panel panelPencarian = new Panel();
        VerticalLayout layoutPencarian = new VerticalLayout();
        layoutPencarian.setMargin(false);
        layoutPencarian.addComponent(initSearch());
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(new Button("Cari", clickEvent -> {
            if (!doValidateRequired()) {
                return;
            }
            doSearch();
        }));
        buttonLayout.addComponent(new Button("Reset", clickEvent -> {
            doReset();
        }));

        layoutPencarian.addComponent(buttonLayout);
        layoutPencarian.setComponentAlignment(buttonLayout, Alignment.MIDDLE_LEFT);
        layout.addComponent(layoutPencarian);

        panelPencarian.setContent(initSearch());
        Panel panelTable = new Panel();
        panelTable.setContent(initTable());

        layout.addComponent(panelTable);
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

    protected abstract Component initSearch();
    protected abstract Component initTable();
    protected abstract String getTitle();
    protected abstract void doSearch();
    protected abstract void doReset();
    protected abstract boolean doValidateRequired();

}
