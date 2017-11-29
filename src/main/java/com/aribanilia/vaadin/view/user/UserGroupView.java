/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.view.user;

import com.aribanilia.vaadin.container.JoinUserGroup;
import com.aribanilia.vaadin.entity.TblGroup;
import com.aribanilia.vaadin.entity.TblUser;
import com.aribanilia.vaadin.entity.TblUserGroup;
import com.aribanilia.vaadin.framework.component.ItemComponent;
import com.aribanilia.vaadin.framework.component.NotificationHelper;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.framework.impl.AbstractDetailScreen;
import com.aribanilia.vaadin.framework.listener.FieldShortcutListener;
import com.aribanilia.vaadin.service.GroupServices;
import com.aribanilia.vaadin.service.UserGroupServices;
import com.aribanilia.vaadin.service.UserServices;
import com.aribanilia.vaadin.util.ValidationHelper;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

@SpringComponent
@UIScope
public class UserGroupView extends AbstractDetailScreen {
    @Autowired private UserGroupServices servicesUserGroup;
    @Autowired private UserServices servicesUser;
    @Autowired private GroupServices servicesGroup;

    private TextField txtUsername, txtName;
    private List<TblUserGroup> listUserGroup;
    private Hashtable<Object, ItemComponent> hItem;
    private TwinColSelect<ItemComponent> selectGroup;

    private TblUser pojoUser;

    private static final Logger logger = LoggerFactory.getLogger(UserGroupView.class);

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

        GridLayout grid = new GridLayout(3, 4);
        int row = 0;

        Label lbl = new Label("Id Pengguna");
        lbl.setWidth("155px");
        grid.addComponent(lbl, 0, row);
        grid.addComponent(txtUsername = new TextField(), 1, row++);
        FieldShortcutListener listener = new FieldShortcutListener() {
            @Override
            public void onEnterKeyPressed() {
                if (ValidationHelper.validateFieldWithoutWarn(txtUsername)) {
                    doSearchUser(txtUsername.getValue());
                }
            }
        };
        listener.install(txtUsername);

        txtUsername.addValueChangeListener(event -> {
           doResetUser();
        });

        grid.addComponent(new Label("Nama Pengguna"), 0, row);
        grid.addComponent(txtName = new TextField(), 1, row++);
        txtName.setEnabled(false);

        grid.addComponent(new Label("Group"), 0, row);
        grid.addComponent(selectGroup = new TwinColSelect<>(), 1, row++);
        createSelectGroupData();

        layout.addComponent(grid);

        return layout;
    }

    private void createSelectGroupData() {
        hItem = new Hashtable<>();
        try {
            List<TblGroup> groups = servicesGroup.queryList();
            if (groups != null && groups.size() > 0) {
                for (TblGroup group : groups
                     ) {
                    hItem.put(group.getGroupId(), new ItemComponent(group.getGroupId(), group.getGroupName()));
                }
            }
            selectGroup.setItems(hItem.values());
            selectGroup.setItemCaptionGenerator(ItemComponent::getCaption);
            selectGroup.setRows(hItem.size() + 1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    private void doSearchUser(String username) {
        try {
            pojoUser = servicesUser.getUser(username);
            if (pojoUser != null) {
                txtName.setValue(pojoUser.getName());
            } else {
                txtName.setValue("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    public void setModeNew() {
        doReset();
        txtUsername.setEnabled(true);
        selectGroup.setEnabled(true);

        btnSave.setVisible(true);
    }

    @Override
    public void setModeUpdate() {
        txtUsername.setEnabled(false);
        selectGroup.setEnabled(true);

        btnSave.setVisible(true);
    }

    @Override
    public void setModeView() {
        txtUsername.setEnabled(false);
        selectGroup.setEnabled(false);

        btnSave.setVisible(false);
    }

    @Override
    protected void doSave() {
        if (!doValidateRequired() || !doValidateValue()) {
            return;
        }
        try {
            Set<ItemComponent> set = selectGroup.getSelectedItems();
            if (getMode() == Constants.APP_MODE.MODE_UPDATE) {
                // Hapus dulu userGroup yang ada
                for (TblUserGroup group : listUserGroup) {
                    servicesUserGroup.delete(group);
                }
            }

            for (ItemComponent itemComponent : set) {
                TblUserGroup userGroup = new TblUserGroup();
                userGroup.setGroupId((Long) itemComponent.getValue());
                userGroup.setUsername(txtUsername.getValue());
                servicesUserGroup.save(userGroup);
            }
            if (getMode() == Constants.APP_MODE.MODE_NEW)
                listener.onAfterUpdated(null);
            else
                listener.onAfterAdded(null);

            doReset();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            NotificationHelper.showNotification(Constants.APP_MESSAGE.ERR_DATA_NOT_SAVE);
        }
    }

    private boolean doValidateRequired() {
        if (ValidationHelper.validateRequired(txtUsername) && ValidationHelper.validateRequired(txtName))
            return true;

        NotificationHelper.showNotification(Constants.APP_MESSAGE.WARN_DATA_MANDATORY);
        return false;
    }

    private boolean doValidateValue() {
        if (selectGroup.getSelectedItems().size() == 0) {
            NotificationHelper.showNotification(Constants.APP_MESSAGE.WARN_DATA_MANDATORY);
            return false;
        }
        return true;
    }

    @Override
    protected void doReset() {
        txtUsername.setValue("");
        selectGroup.deselectAll();
        doResetUser();
    }

    private void doResetUser() {
        txtName.setValue("");
        pojoUser = null;
    }

    @Override
    protected void setContentById(Object pojo) {
        selectGroup.deselectAll();
        try {
            JoinUserGroup join = pojo != null ? ((JoinUserGroup) pojo) : null;
            if (join != null) {
                listUserGroup = servicesUserGroup.getUserGroup(join.getUserGroup_username());
                txtUsername.setValue(join.getUserGroup_username());
                doSearchUser(join.getUserGroup_username());
                for (TblUserGroup userGroup : listUserGroup
                     ) {
                    selectGroup.select(hItem.get(userGroup.getGroupId()));
                }
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
