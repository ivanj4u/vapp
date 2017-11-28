/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.view.user;

import com.aribanilia.vaadin.entity.TblUser;
import com.aribanilia.vaadin.framework.component.NotificationHelper;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.framework.impl.AbstractDetailScreen;
import com.aribanilia.vaadin.framework.impl.AbstractSearchScreen;
import com.aribanilia.vaadin.service.UserServices;
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

import java.util.List;

@UIScope
@SpringView
public class ListUserView extends AbstractSearchScreen implements View {
    @Autowired
    private UserServices servicesUser;
    @Autowired
    private ApplicationContext applicationContext;

    private TextField txtUsername, txtName;
    private AbstractDetailScreen detailScreen;
    private List<TblUser> list;
    private Grid<TblUser> table;

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
        Label lbl = new Label("Id Pengguna");
        lbl.setWidth("100px");
        grid.addComponent(lbl, 0, row);
        grid.addComponent(txtUsername = new TextField(),1, row++);
        grid.addComponent(new Label("Nama"), 0, row);
        grid.addComponent(txtName = new TextField(),1, row++);
    }

    @Override
    protected Component initTableData() {
        table = new Grid<>();
        table.setWidth("100%");
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.setSelectionMode(Grid.SelectionMode.SINGLE);
        table.addItemClickListener(event -> setRowId(event.getItem()));

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
                detailScreen = applicationContext.getBean(UserView.class);
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
        return "40%";
    }

    @Override
    protected String getDetailScreenHeight() {
        return "55%";
    }

    @Override
    protected void doSearch() {
        try {
            list = servicesUser.queryList(txtUsername.getValue(), txtName.getValue());
            table.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onAfterAdded(Object pojo) {
        super.onAfterAdded(pojo);
        if (pojo != null) {
            list.add((TblUser) pojo);
            table.setItems(list);
        }
    }

    @Override
    public void onAfterUpdated(Object pojo) {
        super.onAfterUpdated(pojo);
        if (pojo != null) {
            TblUser userBaru = (TblUser) pojo;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUsername().equals(userBaru.getUsername())) {
                    list.remove(i);
                    list.add(userBaru);
                    break;
                }
            }
            table.setItems(list);
        }
    }

    @Override
    protected void doReset() {
        txtName.setValue("");
        txtUsername.setValue("");
    }

    @Override
    protected boolean validateSearchRequired() {
        return true;
    }

}
