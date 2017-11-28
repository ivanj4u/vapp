/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.view.user;

import com.aribanilia.vaadin.entity.TblUser;
import com.aribanilia.vaadin.framework.component.ComboBoxComponent;
import com.aribanilia.vaadin.framework.component.NotificationHelper;
import com.aribanilia.vaadin.framework.component.PopUpDateField;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.framework.impl.AbstractDetailScreen;
import com.aribanilia.vaadin.service.UserServices;
import com.aribanilia.vaadin.util.ValidationHelper;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Hashtable;

@SpringComponent
public class UserView extends AbstractDetailScreen {
    @Autowired private UserServices servicesUser;

    private TextField txtUsername, txtName, txtEmail, txtPhone;
    private PasswordField txtPassword, txtPasswordConfirm;
    private PopUpDateField txtTglAwal, txtTglAkhir;
    private ComboBox<ComboBoxComponent> cmbStatus;
    private Hashtable<Object, ComboBoxComponent> hComboBox;
    private TblUser pojoUser;

    private static final Logger logger = LoggerFactory.getLogger(UserView.class);

    @Override
    protected void initComponents() {
        VerticalLayout layout = new VerticalLayout();

        initButton();
        layout.addComponent(initDetail());
        layout.addComponent(buttonBar);
        layout.setComponentAlignment(buttonBar, Alignment.MIDDLE_CENTER);
        setContent(layout);
    }

    private Component initDetail() {
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

        grid.addComponent(new Label("Telepon"), 0, row);
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
        grid.addComponent(cmbStatus = new ComboBox<>(), 1, row, 2, row++);

        /**
         * Create Combobox Data
         */
        hComboBox = new Hashtable<>();
        hComboBox.put("1", new ComboBoxComponent("1", "Aktif"));
        hComboBox.put("0", new ComboBoxComponent("0", "Tidak Aktif"));
        cmbStatus.setItems(hComboBox.values());
        cmbStatus.setItemCaptionGenerator(ComboBoxComponent::getCaption);

        layout.addComponent(grid);

        return layout;
    }

    @Override
    public void setModeNew() {
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
        if (!doValidateRequired() || !doValidateValue()) {
            return;
        }
        try {
            if (getMode() == Constants.APP_MODE.MODE_NEW) {
                pojoUser = new TblUser();
                pojoUser.setUsername(txtUsername.getValue());
                pojoUser.setName(txtName.getValue());
                pojoUser.setEmail(txtEmail.getValue());
                pojoUser.setPhone(txtPhone.getValue());
                pojoUser.setStartTime(txtTglAwal.getValueDate());
                pojoUser.setEndTime(txtTglAkhir.getValueDate());
                pojoUser.setLoginFailCount(0);
                pojoUser.setStatus(cmbStatus.getValue().getValue().toString());

                pojoUser.setCreateBy(VaadinSession.getCurrent().getAttribute(TblUser.class).getUsername());
                pojoUser.setCreateDate(new Date());
                pojoUser.setVersi(pojoUser.getCreateDate().getTime());

                servicesUser.save(pojoUser);

                listener.onAfterAdded(pojoUser);
            } else {
                if (!doValidateData()) {
                    return;
                }
                pojoUser.setName(txtName.getValue());
                pojoUser.setEmail(txtEmail.getValue());
                pojoUser.setPhone(txtPhone.getValue());
                pojoUser.setStartTime(txtTglAwal.getValueDate());
                pojoUser.setEndTime(txtTglAkhir.getValueDate());
                pojoUser.setLoginFailCount(0);
                pojoUser.setStatus(cmbStatus.getValue().getValue().toString());

                servicesUser.update(pojoUser);

                listener.onAfterUpdated(pojoUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            NotificationHelper.showNotification(Constants.APP_MESSAGE.ERR_DATA_NOT_SAVE);
        }
    }

    private boolean doValidateRequired() {
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
                cmbStatus.setSelectedItem(hComboBox.get(pojoUser.getStatus()));
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
