/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.view.user;

import com.aribanilia.vaadin.entity.TblUser;
import com.aribanilia.vaadin.framework.component.NotificationHelper;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.framework.impl.AbstractDetailScreen;
import com.aribanilia.vaadin.service.UserServices;
import com.aribanilia.vaadin.util.ValidationHelper;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class DetailUserPasswordView extends AbstractDetailScreen {
    @Autowired private UserServices servicesUser;

    private PasswordField txtPasswordLama, txtPasswordBaru, txtPasswordBaruLagi;

    private static final Logger logger = LoggerFactory.getLogger(DetailUserPasswordView.class);

    @Override
    protected void doSave() {
        if (!doValidate())
            return;
        if (!doValidateValue())
            return;
        try {
            TblUser user = VaadinSession.getCurrent().getAttribute(TblUser.class);
            user.setPassword(txtPasswordBaru.getValue());
            servicesUser.update(user);

            NotificationHelper.showNotification("Password berhasil diubah");
            listener.onAfterUpdated(true);

            // Replace User Session
            VaadinSession.getCurrent().setAttribute(TblUser.class, user);
            doReset();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    protected void doCancel() {
        listener.onAfterUpdated(true);
    }

    @Override
    protected boolean doValidate() {
        if (ValidationHelper.validateRequired(txtPasswordLama)
                && ValidationHelper.validateRequired(txtPasswordBaru)
                && ValidationHelper.validateRequired(txtPasswordBaruLagi))
            return true;

        NotificationHelper.showNotification(Constants.APP_MESSAGE.WARN_DATA_MANDATORY);
        return false;
    }

    private boolean doValidateValue() {
        if (txtPasswordLama.getValue().equals(txtPasswordBaru.getValue())) {
            NotificationHelper.showNotification("Password Baru tidak valid", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        if (!txtPasswordBaru.getValue().equals(txtPasswordBaruLagi.getValue())) {
            NotificationHelper.showNotification("Password Baru tidak sama", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    protected void doReset() {
        txtPasswordLama.setValue("");
        txtPasswordBaru.setValue("");
        txtPasswordBaruLagi.setValue("");
    }

    @Override
    protected void setContentById(Object pojo) {

    }

    @Override
    protected Component initDetail() {
        VerticalLayout layout = new VerticalLayout();
        GridLayout grid = new GridLayout(2, 4);
        int row = 0;
        Label lbl = new Label("Password Lama");
        lbl.setWidth("185px");
        grid.addComponent(lbl, 0, row);
        grid.addComponent(txtPasswordLama = new PasswordField(), 1, row++);

        grid.addComponent(new Label("Password Baru"), 0, row);
        grid.addComponent(txtPasswordBaru = new PasswordField(), 1, row++);

        grid.addComponent(new Label("Konfirmasi Password Baru"), 0, row);
        grid.addComponent(txtPasswordBaruLagi = new PasswordField(), 1, row++);

        layout.addComponent(grid);
        return layout;
    }

    @Override
    public void setModeNew() {

    }

    @Override
    public void setModeUpdate() {

    }

    @Override
    public void setModeView() {

    }

    @Override
    public boolean isAuthorizedToAdd() {
        return true;
    }

    @Override
    public boolean isAuthorizedToUpdate() {
        return true;
    }

    @Override
    public boolean isAuthorizedToView() {
        return true;
    }

    @Override
    public boolean isAuthorizedToDelete() {
        return true;
    }
}
