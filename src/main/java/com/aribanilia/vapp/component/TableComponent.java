/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.component;

import com.vaadin.data.ValueProvider;
import com.vaadin.ui.Grid;

public class TableComponent extends Grid<Object> {

    public TableComponent() {
        super();
    }

    public void addContainer(String propertyName) {
        setColumnId(propertyName, addColumn(propertyName));

    }

}
