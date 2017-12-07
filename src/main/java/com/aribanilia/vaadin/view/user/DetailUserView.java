/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.view.user;

import com.aribanilia.vaadin.entity.TblUser;
import com.aribanilia.vaadin.framework.component.ItemComponent;
import com.aribanilia.vaadin.framework.component.NotificationHelper;
import com.aribanilia.vaadin.framework.component.PopUpComboBox;
import com.aribanilia.vaadin.framework.component.PopUpDateField;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.framework.impl.AbstractDetailScreen;
import com.aribanilia.vaadin.services.UserServices;
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
public class DetailUserView extends AbstractDetailScreen {
    @Autowired private UserServices servicesUser;

    private TextField txtUsername, txtName, txtEmail, txtPhone;
    private PasswordField txtPassword, txtPasswordConfirm;
    private PopUpDateField txtTglAwal, txtTglAkhir;
    private PopUpComboBox cmbStatus;
    private TblUser pojoUser;

    private static final Logger logger = LoggerFactory.getLogger(DetailUserView.class);

    @Override
    protected Component initDetail() {
        VerticalLayout layout = new VerticalLayout();
        layout.addStyleName(ValoTheme.PANEL_WELL);

        GridLayout grid = new GridLayout(4, 8);
        int row = 0;

        Label lbl = new Label("Id Pengguna");
        lbl.setWidth("155px");
        grid.addComponent(lbl, 0, row);
        grid.addComponent(txtUsername = new TextField(), 1, row, 2, row++);

        grid.addComponent(new Label("Nama"), 0, row);
        grid.addComponent(txtName = new TextField(), 1, row, 2, row++);

        grid.addComponent(new Label("Email"), 0, row);
        grid.addComponent(txtEmail = new TextField(), 1, row, 2, row++);

        grid.addComponent(new Label("Phone"), 0, row);
        grid.addComponent(txtPhone = new TextField(), 1, row, 2, row++);

        grid.addComponent(new Label("Password"), 0, row);
        grid.addComponent(txtPassword = new PasswordField(), 1, row, 2, row++);

        grid.addComponent(new Label("Konfirmasi Password"), 0, row);
        grid.addComponent(txtPasswordConfirm = new PasswordField(), 1, row, 2, row++);

        grid.addComponent(new Label("Tgl Berlaku"), 0, row);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(txtTglAwal = new PopUpDateField());
        horizontalLayout.addComponent(new Label(" s/d "));
        horizontalLayout.addComponent(txtTglAkhir = new PopUpDateField());
        grid.addComponent(horizontalLayout, 1, row, 2, row++);

        grid.addComponent(new Label("Status"), 0, row);
        grid.addComponent(cmbStatus = new PopUpComboBox(), 1, row, 2, row++);
        createComboBoxData();

        layout.addComponent(grid);

        return layout;
    }

    private void createComboBoxData() {
        cmbStatus.addItem(new ItemComponent("1", "Aktif"));
        cmbStatus.addItem(new ItemComponent("0", "Tidak Aktif"));
    }

    @Override
    public void setModeNew() {
        doReset();
        txtUsername.setEnabled(true);
        txtName.setEnabled(true);
        txtEmail.setEnabled(true);
        txtPhone.setEnabled(true);
        txtPassword.setEnabled(true);
        txtPasswordConfirm.setEnabled(true);
        txtTglAwal.setEnabled(true);
        txtTglAkhir.setEnabled(true);
        cmbStatus.setEnabled(true);

        btnSave.setVisible(true);
    }

    @Override
    public void setModeUpdate() {
        txtUsername.setEnabled(false);
        txtName.setEnabled(true);
        txtEmail.setEnabled(true);
        txtPhone.setEnabled(true);
        txtPassword.setEnabled(true);
        txtPasswordConfirm.setEnabled(true);
        txtTglAwal.setEnabled(true);
        txtTglAkhir.setEnabled(true);
        cmbStatus.setEnabled(true);

        btnSave.setVisible(true);
    }

    @Override
    public void setModeView() {
        txtUsername.setEnabled(false);
        txtName.setEnabled(false);
        txtEmail.setEnabled(false);
        txtPhone.setEnabled(false);
        txtPassword.setEnabled(false);
        txtPasswordConfirm.setEnabled(false);
        txtTglAwal.setEnabled(false);
        txtTglAkhir.setEnabled(false);
        cmbStatus.setEnabled(false);

        btnSave.setVisible(false);
    }

    @Override
    protected void doSave() {
        if (!doValidate() || !doValidateValue()) {
            return;
        }
        try {
            if (getMode() == Constants.APP_MODE.MODE_NEW) {
                pojoUser = new TblUser();
                pojoUser.setUsername(txtUsername.getValue());
            } else {
                if (!doValidateData()) {
                    return;
                }
            }
            pojoUser.setName(txtName.getValue());
            pojoUser.setEmail(txtEmail.getValue());
            pojoUser.setPhone(txtPhone.getValue());
            pojoUser.setPassword(txtPassword.getValue());
            pojoUser.setStartTime(txtTglAwal.getValueDate());
            pojoUser.setEndTime(txtTglAkhir.getValueDate());
            pojoUser.setLoginFailCount(0);
            pojoUser.setStatus(cmbStatus.getValueItem().toString());

            if (getMode() == Constants.APP_MODE.MODE_NEW) {
                servicesUser.save(pojoUser);
                listener.onAfterAdded(pojoUser);
            } else {
                servicesUser.update(pojoUser);
                listener.onAfterUpdated(pojoUser);
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
        if (ValidationHelper.validateRequired(txtUsername) && ValidationHelper.validateRequired(txtName)
                && ValidationHelper.validateRequired(txtEmail) && ValidationHelper.validateRequired(txtPhone)
                && ValidationHelper.validateRequired(txtPassword) && ValidationHelper.validateRequired(txtPasswordConfirm)
                && ValidationHelper.validateRequired(txtTglAwal) && ValidationHelper.validateRequired(txtTglAkhir)
                && ValidationHelper.validateValueNotNull(cmbStatus.getValue()))
            return true;

        NotificationHelper.showNotification(Constants.APP_MESSAGE.WARN_DATA_MANDATORY);
        return false;
    }

    private boolean doValidateValue() {
        if (!txtPassword.getValue().equals(txtPasswordConfirm.getValue())) {
            NotificationHelper.showNotification("Password tidak sesuai");
            return false;
        }
        if (txtTglAwal.getValue().toEpochDay() > txtTglAkhir.getValue().toEpochDay()) {
            NotificationHelper.showNotification("Tanggal Awal tidak benar");
            return false;
        }
        return true;
    }

    private boolean doValidateData() throws Exception {
        TblUser userDatabase = servicesUser.getUser(pojoUser.getUsername());
        if (userDatabase.getVersi() > pojoUser.getVersi()) {
            NotificationHelper.showNotification(Constants.APP_MESSAGE.ERR_DATA_IN_VALID);
            return false;
        }
        return true;
    }

    @Override
    protected void doReset() {
        txtUsername.setValue("");
        txtName.setValue("");
        txtEmail.setValue("");
        txtPhone.setValue("");
        txtPassword.setValue("");
        txtPasswordConfirm.setValue("");
        txtTglAwal.setValue(null);
        txtTglAkhir.setValue(null);
        cmbStatus.setValue(null);

        pojoUser = null;
    }

    @Override
    protected void setContentById(Object pojo) {
        try {
            pojoUser = pojo != null ? ((TblUser) pojo) : null;
            if (pojoUser != null) {
                txtUsername.setValue(pojoUser.getUsername());
                txtName.setValue(pojoUser.getName());
                txtEmail.setValue(pojoUser.getEmail());
                txtPhone.setValue(pojoUser.getPhone());
                txtPassword.setValue(pojoUser.getPassword());
                txtPasswordConfirm.setValue(pojoUser.getPassword());
                txtTglAwal.setValueDate(pojoUser.getStartTime());
                txtTglAkhir.setValueDate(pojoUser.getEndTime());
                cmbStatus.setValueItem(pojoUser.getStatus());
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
}
