/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.view.param;

import com.aribanilia.vaadin.entity.TblParam;
import com.aribanilia.vaadin.framework.component.NotificationHelper;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.framework.impl.AbstractDetailScreen;
import com.aribanilia.vaadin.framework.impl.AbstractSearchScreen;
import com.aribanilia.vaadin.services.ParamServices;
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
public class ListParamView extends AbstractSearchScreen implements View {

    @Autowired private ParamServices servicesParam;
    @Autowired private ApplicationContext applicationContext;

    private TextField txtKey;
    private AbstractDetailScreen detailScreen;
    private Grid<TblParam> table;
    private List<TblParam> list;

    private final String KEY_ = "Id Param";
    private final String VALUE_ = "Nilai Param";
    private final String DESC_ = "Deskripsi";

    private static final Logger logger = LoggerFactory.getLogger(ListParamView.class);

    @Override
    protected int getGridColumn() {
        return 2;
    }

    @Override
    protected int getGridRow() {
        return 2;
    }

    @Override
    protected void initGridComponent() {
        Label lbl = new Label("Id Param");
        lbl.setWidth("110px");
        grid.addComponent(lbl, 0, row);
        grid.addComponent(txtKey = new TextField(), 1, row++);
    }

    @Override
    protected Component getTableData() {
        return table;
    }

    @Override
    protected void initTableData() {
        list = new ArrayList<>();
        table = (Grid<TblParam>) initTable();
        table.addColumn(TblParam::getKey).setCaption(KEY_);
        table.addColumn(TblParam::getValue).setCaption(VALUE_);
        table.addColumn(TblParam::getDescription).setCaption(DESC_);
    }

    @Override
    protected AbstractDetailScreen getDetailScreen() {
        if (detailScreen == null) {
            try {
                detailScreen = applicationContext.getBean(DetailParamView.class);
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
        return "Pengaturan Sys Param";
    }

    @Override
    protected String getDetailScreenWidth() {
        return "45%";
    }

    @Override
    protected String getDetailScreenHeight() {
        return "60%";
    }

    @Override
    protected void doSearch() {
        try {
            list = servicesParam.queryList(txtKey.getValue());
            table.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            NotificationHelper.showNotification(Constants.APP_MESSAGE.ERR_DATA_SEARCH);
        }
    }

    @Override
    protected void doReset() {
        txtKey.setValue("");

        list.clear();
        table.setItems(list);
    }

    @Override
    protected boolean validateSearchRequired() {
        return true;
    }
}
