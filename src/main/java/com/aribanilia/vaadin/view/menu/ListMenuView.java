/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.view.menu;

import com.aribanilia.vaadin.entity.TblMenu;
import com.aribanilia.vaadin.framework.component.NotificationHelper;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.framework.impl.AbstractDetailScreen;
import com.aribanilia.vaadin.framework.impl.AbstractSearchScreen;
import com.aribanilia.vaadin.service.MenuServices;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

@UIScope
@SpringView
public class ListMenuView extends AbstractSearchScreen implements View {
    @Autowired
    private MenuServices servicesMenu;
    @Autowired private ApplicationContext applicationContext;

    private TextField txtMenuId, txtMenuName, txtMenuParent;
    private AbstractDetailScreen detailScreen;
    private List<TblMenu> list;
    private Grid<TblMenu> table;

    private final String MENU_ID = "Id Menu";
    private final String MENU_NAME = "Nama Menu";
    private final String MENU_CLASS = "Class Menu";
    private final String MENU_PARENT = "Parent Menu";

    private static final Logger logger = LoggerFactory.getLogger(ListMenuView.class);

    @Override
    protected int getGridColumn() {
        return 2;
    }

    @Override
    protected int getGridRow() {
        return 4;
    }

    @Override
    protected void initGridComponent() {
        Label lbl = new Label("Id Menu");
        lbl.setWidth("115px");

        grid.addComponent(lbl, 0, row);
        grid.addComponent(txtMenuId = new TextField(), 1, row++);

        grid.addComponent(new Label("Nama Menu"), 0, row);
        grid.addComponent(txtMenuName = new TextField(), 1, row++);

        grid.addComponent(new Label("Parent Menu"), 0, row);
        grid.addComponent(txtMenuParent = new TextField(), 1, row++);
    }

    @Override
    protected Component getTableData() {
        return table;
    }

    @Override
    protected void initTableData() {
        list = new ArrayList<>();
        table = (Grid<TblMenu>) initTable();
        table.addColumn(TblMenu::getMenuId).setCaption(MENU_ID);
        table.addColumn(TblMenu::getMenuName).setCaption(MENU_NAME);
        table.addColumn(TblMenu::getMenuClass).setCaption(MENU_CLASS);
        table.addColumn(TblMenu::getParentId).setCaption(MENU_PARENT);
    }

    @Override
    protected AbstractDetailScreen getDetailScreen() {
        if (detailScreen == null) {
            try {
                detailScreen = applicationContext.getBean(DetailMenuView.class);
                detailScreen.setListener(this);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                NotificationHelper.showNotification(Constants.APP_MESSAGE.ERR_DATA_GET_DETAIL);
            }
        }
        return detailScreen;
    }

    @Override
    protected String getDetailScreenTitle() {
        return "Pengaturan Menu";
    }

    @Override
    protected String getDetailScreenWidth() {
        return "60%";
    }

    @Override
    protected String getDetailScreenHeight() {
        return "80%";
    }

    @Override
    protected void doSearch() {
        try {
            list = servicesMenu.queryList(txtMenuId.getValue(), txtMenuName.getValue(), txtMenuParent.getValue());
            table.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            NotificationHelper.showNotification(Constants.APP_MESSAGE.ERR_DATA_SEARCH);
        }
    }

    @Override
    protected void doReset() {
        txtMenuId.setValue("");
        txtMenuName.setValue("");
        txtMenuParent.setValue("");
        list.clear();
        table.setItems(list);

    }

    @Override
    protected boolean validateSearchRequired() {
        return true;
    }
}
