/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.view.admin;

import com.aribanilia.vaadin.entity.TblUser;
import com.aribanilia.vaadin.framework.component.NotificationHelper;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.framework.impl.AbstractDetailScreen;
import com.aribanilia.vaadin.framework.impl.AbstractSearchScreen;
import com.aribanilia.vaadin.loader.MenuLoader;
import com.aribanilia.vaadin.service.UserServices;
import com.aribanilia.vaadin.util.ValidationHelper;
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

import java.util.List;

@UIScope
@SpringView
public class ListUserView extends AbstractSearchScreen implements View {
    @Autowired
    private UserServices servicesUser;

    private TextField txtUsername, txtName;
    private AbstractDetailScreen detailScreen;
    private List<TblUser> list;

    private final String USERNAME = "Id Pengguna";
    private final String NAME = "Nama";
    private final String EMAIL = "Email";
    private final String TELP = "No Telp";

    private static final Logger logger = LoggerFactory.getLogger(ListUserView.class);

    @Override
    protected int getGridColumn() {
        return 2;
    }

    @Override
    protected int getGridRow() {
        return 3;
    }

    @Override
    protected void initGridComponent() {
        grid.addComponent(new Label("Id Pengguna"), 0, row);
        grid.addComponent(txtUsername = new TextField(),1, row++);
        grid.addComponent(new Label("Nama"), 0, row);
        grid.addComponent(txtName = new TextField(),1, row++);
    }

    @Override
    protected Component initTableData() {
        Grid<TblUser> table = new Grid<>();
        table.setItems(list);
        table.setWidth("100%");
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.setSelectionMode(Grid.SelectionMode.SINGLE);
        table.addItemClickListener(event -> {
            setRowId(event.getItem());
        });

        table.addColumn(TblUser::getUsername).setCaption(USERNAME);
        table.addColumn(TblUser::getName).setCaption(NAME);
        table.addColumn(TblUser::getEmail).setCaption(EMAIL);
        table.addColumn(TblUser::getPhone).setCaption(TELP);

        return table;
    }

    @Override
    protected AbstractDetailScreen getDetailScreen() {
        if (detailScreen == null) {
            try {
                detailScreen = (AbstractDetailScreen) Class.forName("com.aribanilia.vaadin.view.admin.UserView").newInstance();
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
        return "Pendaftaran Pengguna";
    }

    @Override
    protected String getDetailScreenWidth() {
        return "50%";
    }

    @Override
    protected String getDetailScreenHeight() {
        return "70%";
    }

    @Override
    protected void doSearch() {
        String username = "?";
        String name = "?";
        if (ValidationHelper.validateFieldWithoutWarn(txtUsername)) {
            username = txtUsername.getValue();
        }
        if (ValidationHelper.validateFieldWithoutWarn(txtName)) {
            name = "%" + txtName + "%";
        }
        try {
            list = servicesUser.queryList(username, name);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

    }

    @Override
    protected void doReset() {

    }

    @Override
    protected boolean validateSearchRequired() {
        return false;
    }
}
