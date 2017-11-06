/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.view;

import com.aribanilia.vapp.entity.TblPriviledge;
import com.aribanilia.vapp.framework.AbstractSearchScreen;
import com.aribanilia.vapp.service.PriviledgeServices;
import com.aribanilia.vapp.util.ValidationHelper;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UIScope
@SpringView
public class DashboardView extends AbstractSearchScreen implements View {

    @Autowired
    private PriviledgeServices servicesPriviledge;

    private TextField txtGroupId;
    private Grid table;

    private final String GROUP_ID = "Group Id";
    private final String MENU_ID = "Menu Id";

    @Override
    protected Component initSearch() {
        GridLayout grid = new GridLayout(2, 3);
        int row = 0;
        grid.addComponent(txtGroupId = new TextField("Id Group"), 0, row, 1, row++);

        return grid;
    }

    @Override
    protected Component initTable() {
        table = new Grid();
//        table.addColumn(GROUP_ID);
//        table.addColumn(MENU_ID);

        return table;
    }

    @Override
    protected String getTitle() {
        return "Dashboard View";
    }

    @Override
    protected void doSearch() {
        try {
            List<TblPriviledge> list = servicesPriviledge.getGroupPriviledge(new Long(txtGroupId.getValue()));
            if (list != null) {
                table.setItems(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doReset() {
        txtGroupId.setValue("");
    }

    @Override
    protected boolean doValidateRequired() {
        if (ValidationHelper.validateRequired(txtGroupId))
            return true;
        return false;
    }
}
