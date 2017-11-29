/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.view.user;

import com.aribanilia.vaadin.container.JoinUserGroup;
import com.aribanilia.vaadin.framework.component.NotificationHelper;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.framework.impl.AbstractDetailScreen;
import com.aribanilia.vaadin.framework.impl.AbstractSearchScreen;
import com.aribanilia.vaadin.service.UserGroupServices;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

@UIScope
@SpringView
public class ListUserGroupView extends AbstractSearchScreen implements View {
    @Autowired
    private UserGroupServices servicesUserGroup;

    @Autowired
    private ApplicationContext applicationContext;

    private TextField txtGroupId, txtUsername;
    private AbstractDetailScreen detailScreen;
    private List<JoinUserGroup> list;
    private Grid<JoinUserGroup> table;

    private final String GROUP_ID = "Id Group";
    private final String GROUP_NAME = "Nama Group";
    private final String USERNAME = "Id Pengguna";
    private final String NAME = "Nama Pengguna";

    private static final Logger logger = LoggerFactory.getLogger(ListUserGroupView.class);

    @Override
    protected int getGridColumn() {
        return 2;
    }

    @Override
    protected int getGridRow() {
        return 3;
    }

    @Override
    protected void beforeInitComponent() {
        list = new ArrayList<>();

        table = new Grid<>();
        table.setWidth("100%");
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.setSelectionMode(Grid.SelectionMode.SINGLE);
        table.addItemClickListener(event -> setRowId(event.getItem()));
    }

    @Override
    protected void initGridComponent() {
        Label lbl = new Label("Id Group");
        lbl.setWidth("100px");
        grid.addComponent(lbl, 0, row);
        grid.addComponent(txtGroupId = new TextField(),1, row++);
        grid.addComponent(new Label("Id Pengguna"), 0, row);
        grid.addComponent(txtUsername = new TextField(),1, row++);
    }

    @Override
    protected Component getTableData() {
        return table;
    }

    @Override
    protected void initTableData() {
        table.addColumn(JoinUserGroup::getGroup_groupId).setCaption(GROUP_ID);
        table.addColumn(JoinUserGroup::getGroup_groupName).setCaption(GROUP_NAME);
        table.addColumn(JoinUserGroup::getUser_username).setCaption(USERNAME);
        table.addColumn(JoinUserGroup::getUser_name).setCaption(NAME);
    }

    @Override
    protected AbstractDetailScreen getDetailScreen() {
        if (detailScreen == null) {
            try {
                detailScreen = applicationContext.getBean(UserGroupView.class);
                detailScreen.setListener(this);
            } catch (Exception e) {
                logger.error(e.getMessage());
                NotificationHelper.showNotification(Constants.APP_MESSAGE.ERR_DATA_GET_DETAIL);
            }
        }
        return detailScreen;
    }

    @Override
    protected String getDetailScreenTitle() {
        return "Parameter User Group";
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
            list = servicesUserGroup.searchUserGroup(txtGroupId.getValue(), txtUsername.getValue());
            table.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    protected void doReset() {
        txtGroupId.setValue("");
        txtUsername.setValue("");
        list.clear();
        table.setItems(list);
    }

    @Override
    protected boolean validateSearchRequired() {
        return true;
    }

}
