/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.view.menu;

import com.aribanilia.vaadin.entity.TblMenu;
import com.aribanilia.vaadin.framework.component.ItemComponent;
import com.aribanilia.vaadin.framework.component.NotificationHelper;
import com.aribanilia.vaadin.framework.component.PopUpComboBox;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.framework.impl.AbstractDetailScreen;
import com.aribanilia.vaadin.services.MenuServices;
import com.aribanilia.vaadin.util.ValidationHelper;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class DetailMenuView extends AbstractDetailScreen {
    @Autowired private MenuServices servicesMenu;

    private TextField txtMenuId, txtMenuName, txtParentId, txtMenuClass, txtMenuParam, txtPosition;
    private PopUpComboBox cmbHaveChild;

    private TblMenu pojoMenu;

    private static final Logger logger = LoggerFactory.getLogger(DetailMenuView.class);

    @Override
    protected Component initDetail() {
        VerticalLayout layout = new VerticalLayout();
        layout.addStyleName(ValoTheme.PANEL_WELL);

        GridLayout grid = new GridLayout(2, 8);
        int row = 0;

        Label lbl = new Label("Id Menu");
        lbl.setWidth("135px");

        grid.addComponent(lbl, 0, row);
        grid.addComponent(txtMenuId = new TextField(), 1, row++);

        grid.addComponent(new Label("Nama Menu"), 0, row);
        grid.addComponent(txtMenuName = new TextField(), 1, row++);

        grid.addComponent(new Label("Class Menu"), 0, row);
        grid.addComponent(txtMenuClass = new TextField(), 1, row++);

        grid.addComponent(new Label("Parameter"), 0, row);
        grid.addComponent(txtMenuParam = new TextField(), 1, row++);

        grid.addComponent(new Label("No Urut"), 0, row);
        grid.addComponent(txtPosition = new TextField(), 1, row++);

        grid.addComponent(new Label("Have Child"), 0, row);
        grid.addComponent(cmbHaveChild = new PopUpComboBox(), 1, row++);
        cmbHaveChild.addItem(new ItemComponent("0", "Tidak"));
        cmbHaveChild.addItem(new ItemComponent("1", "Iya"));

        grid.addComponent(new Label("Parent Menu"), 0, row);
        grid.addComponent(txtParentId = new TextField(), 1, row++);

        layout.addComponent(grid);
        return layout;
    }

    @Override
    protected void doSave() {
        if (!doValidate())
            return;
        if (!doValidateValue())
            return;

        try {
            if (getMode() == Constants.APP_MODE.MODE_NEW) {
                pojoMenu = new TblMenu();
                pojoMenu.setMenuId(txtMenuId.getValue());
            }
            pojoMenu.setMenuName(txtMenuName.getValue());
            pojoMenu.setParentId(txtParentId.getValue());
            pojoMenu.setHaveChild(cmbHaveChild.getValueItem().toString());
            pojoMenu.setPosition(Long.parseLong(txtPosition.getValue()));
            if (pojoMenu.getHaveChild().equals("0"))
                pojoMenu.setMenuClass(txtMenuClass.getValue());
            if (ValidationHelper.validateFieldWithoutWarn(txtMenuParam))
                pojoMenu.setParam(txtMenuParam.getValue());

            if (getMode() == Constants.APP_MODE.MODE_NEW) {
                servicesMenu.save(pojoMenu);
                listener.onAfterAdded(pojoMenu);
            } else {
                servicesMenu.update(pojoMenu);
                listener.onAfterUpdated(pojoMenu);
            }
            doReset();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            NotificationHelper.showNotification(Constants.APP_MESSAGE.ERR_DATA_NOT_SAVE);
        }
    }

    @Override
    protected boolean doValidate() {
        if (ValidationHelper.validateRequired(txtMenuId) && ValidationHelper.validateRequired(txtMenuName)
                && ValidationHelper.validateRequired(txtPosition) && ValidationHelper.validateRequired(txtParentId)
                && ValidationHelper.validateValueNotNull(cmbHaveChild.getValueItem()))
            return true;

        NotificationHelper.showNotification(Constants.APP_MESSAGE.WARN_DATA_MANDATORY);
        return false;
    }

    private boolean doValidateValue() {
        if ("0".equals(cmbHaveChild.getValueItem().toString())) {
            if (!ValidationHelper.validateRequired(txtMenuClass)) {
                NotificationHelper.showNotification(Constants.APP_MESSAGE.WARN_DATA_MANDATORY);
                return false;
            }
        }
        return true;
    }

    @Override
    protected void doReset() {
        txtMenuId.setValue("");
        txtMenuName.setValue("");
        txtMenuClass.setValue("");
        txtMenuParam.setValue("");
        txtPosition.setValue("");
        txtParentId.setValue("");

        pojoMenu = null;
    }

    @Override
    protected void setContentById(Object pojo) {
        pojoMenu = pojo != null ? (TblMenu) pojo : null;
        try {
            if (pojoMenu != null) {
                txtMenuId.setValue(pojoMenu.getMenuId());
                txtMenuName.setValue(pojoMenu.getMenuName());
                txtMenuParam.setValue(pojoMenu.getParam() != null ? pojoMenu.getParam() : "");
                txtMenuClass.setValue(pojoMenu.getMenuClass() != null ? pojoMenu.getMenuClass() : "");
                txtParentId.setValue(pojoMenu.getParentId());
                txtPosition.setValue(String.valueOf(pojoMenu.getPosition()));
                cmbHaveChild.setValueItem(pojoMenu.getHaveChild());
            } else {
                NotificationHelper.showNotification(Constants.APP_MESSAGE.INFO_DATA_NOT_EXIST);
                doCancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            NotificationHelper.showNotification(Constants.APP_MESSAGE.ERR_DATA_GET_DETAIL);
        }
    }

    @Override
    public void setModeNew() {
        doReset();

        txtMenuId.setEnabled(true);
        txtMenuName.setEnabled(true);
        txtMenuParam.setEnabled(true);
        txtMenuClass.setEnabled(true);
        txtParentId.setEnabled(true);
        txtPosition.setEnabled(true);
        cmbHaveChild.setEnabled(true);

        btnSave.setVisible(true);
    }

    @Override
    public void setModeUpdate() {
        txtMenuId.setEnabled(false);
        txtMenuName.setEnabled(true);
        txtMenuParam.setEnabled(true);
        txtMenuClass.setEnabled(true);
        txtParentId.setEnabled(true);
        txtPosition.setEnabled(true);
        cmbHaveChild.setEnabled(true);

        btnSave.setVisible(true);
    }

    @Override
    public void setModeView() {
        txtMenuId.setEnabled(false);
        txtMenuName.setEnabled(false);
        txtMenuParam.setEnabled(false);
        txtMenuClass.setEnabled(false);
        txtParentId.setEnabled(false);
        txtPosition.setEnabled(false);
        cmbHaveChild.setEnabled(false);

        btnSave.setVisible(false);
    }
}
