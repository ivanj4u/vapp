/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.view.menu;

import com.aribanilia.vaadin.framework.impl.AbstractDetailScreen;
import com.aribanilia.vaadin.framework.impl.AbstractSearchScreen;
import com.vaadin.ui.Component;

public class ListMenuView extends AbstractSearchScreen {

    @Override
    protected int getGridColumn() {
        return 0;
    }

    @Override
    protected int getGridRow() {
        return 0;
    }

    @Override
    protected void initGridComponent() {

    }

    @Override
    protected Component getTableData() {
        return null;
    }

    @Override
    protected void initTableData() {

    }

    @Override
    protected AbstractDetailScreen getDetailScreen() {
        return null;
    }

    @Override
    protected String getDetailScreenTitle() {
        return null;
    }

    @Override
    protected String getDetailScreenWidth() {
        return null;
    }

    @Override
    protected String getDetailScreenHeight() {
        return null;
    }

    @Override
    protected void doSearch() {

    }

    @Override
    protected void doReset() {

    }

    @Override
    protected boolean validateSearchRequired() {
        return false;
    }
}
