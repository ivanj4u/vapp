/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.view.user;

import com.aribanilia.vaadin.container.TreeMenuContainer;
import com.aribanilia.vaadin.entity.TblMenu;
import com.aribanilia.vaadin.entity.TblUserGroup;
import com.aribanilia.vaadin.framework.component.NotificationHelper;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.framework.impl.AbstractDetailScreen;
import com.aribanilia.vaadin.service.GroupServices;
import com.aribanilia.vaadin.service.MenuServices;
import com.aribanilia.vaadin.service.PriviledgeServices;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Hashtable;
import java.util.List;

@SpringComponent
@UIScope
public class DetailPriviledgeView extends AbstractDetailScreen {

    @Autowired private GroupServices servicesGroup;
    @Autowired private PriviledgeServices servicesPriviledge;
    @Autowired private MenuServices servicesMenu;

    private TextField txtGroupId, txtGroupName;
    private Button btnEdit;
    private TreeGrid<TreeMenuContainer> tree;
    private Hashtable<String, TreeMenuContainer> hMenu;
    private TreeMenuContainer selectedItem;
    private Window wMenuContainerView;
    private DetailMenuContainerView menuContainerView;

    private static final Logger logger = LoggerFactory.getLogger(DetailPriviledgeView.class);

    @Override
    protected void doSave() {

    }

    @Override
    protected void doReset() {
        txtGroupId.setValue("");
        txtGroupName.setValue("");
        selectedItem = null;
    }

    @Override
    protected void setContentById(Object pojo) {
        try {
            TblUserGroup userGroup = pojo != null ? (TblUserGroup) pojo : null;
            if (userGroup != null) {
                txtGroupId.setValue(String.valueOf(userGroup.getGroupId()));
                txtGroupName.setValue(userGroup.getUsername());


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
    protected void beforeInitComponent() {
        hMenu = new Hashtable<>();
    }

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

        Label lbl = new Label("Id Group");
        lbl.setWidth("110px");

        grid.addComponent(lbl, 0, row);
        grid.addComponent(txtGroupId = new TextField(), 1, row++);

        grid.addComponent(new Label("Nama Group"), 0, row);
        grid.addComponent(txtGroupName = new TextField(), 1, row++);

        layout.addComponent(grid);
        layout.addComponent(btnEdit = new Button("Ubah"));
        btnEdit.addListener(event -> {
            if (selectedItem == null) {
                NotificationHelper.showNotification(Constants.APP_MESSAGE.WARN_DATA_MUST_BE_SELECTED);
                return;
            }
            showMenuContainerWindow();
        });

        layout.setComponentAlignment(btnEdit, Alignment.MIDDLE_RIGHT);
        layout.addComponent(tree = new TreeGrid<>());

        tree.addItemClickListener(event -> {
            if (event.getItem() != null) {
                selectedItem = event.getItem();
            }
        });

        return layout;
    }

    private void showMenuContainerWindow() {
        if (selectedItem == null) {
            NotificationHelper.showNotification(Constants.APP_MESSAGE.WARN_DATA_MUST_BE_SELECTED);
            return;
        }
        if (menuContainerView == null) {
            try {
                menuContainerView = new DetailMenuContainerView(selectedItem, result -> {
                    getUI().removeWindow(wMenuContainerView);
                    TreeMenuContainer menuContainer = (TreeMenuContainer) result;
                    /**
                     * Remove old menu
                     */
                    TreeDataProvider<TreeMenuContainer> dataProvider = (TreeDataProvider<TreeMenuContainer>) tree.getDataProvider();
                    TreeData<TreeMenuContainer> treeData = dataProvider.getTreeData();
                    treeData.removeItem(hMenu.get(menuContainer.getMenuId()));
                    /**
                     * Put new menu
                     */
                    hMenu.put(menuContainer.getMenuId(), menuContainer);
                    treeData.addItem(menuContainer.getParentMenu(), menuContainer);
                    dataProvider.refreshAll();
                });
                wMenuContainerView = new Window();
                wMenuContainerView.setContent(menuContainerView);
                wMenuContainerView.setModal(true);
                wMenuContainerView.setWidth("55%");
                wMenuContainerView.setHeight("45%");
                wMenuContainerView.addStyleName(ValoTheme.PANEL_WELL);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }

    @Override
    protected void afterInitComponent() {
        createMenuData();
    }

    @SuppressWarnings("unchecked")
    private void createMenuData() {
        tree.setWidth("100%");
        tree.addColumn(TreeMenuContainer::getMenuId).setCaption("Id Menu");
        tree.addColumn(TreeMenuContainer::getMenuName).setCaption("Nama Menu");
        tree.addColumn(TreeMenuContainer::getIsAdd).setCaption("Tambah");
        tree.addColumn(TreeMenuContainer::getIsUpdate).setCaption("Ubah");
        tree.addColumn(TreeMenuContainer::getIsView).setCaption("Lihat");
        tree.addColumn(TreeMenuContainer::getIsDelete).setCaption("Hapus");

        TreeDataProvider<TreeMenuContainer> dataProvider = (TreeDataProvider<TreeMenuContainer>) tree.getDataProvider();
        TreeData<TreeMenuContainer> treeData = dataProvider.getTreeData();

        TreeMenuContainer menuContainer;
        List<TblMenu> listMenu = servicesMenu.getAllMenu();
        for (TblMenu menu : listMenu) {
            if (menu.getParentId().equals("0")) {
                menuContainer = packContainer(menu, true);
            } else {
                menuContainer = packContainer(menu, false);
            }
            hMenu.put(menu.getMenuId(), menuContainer);
            treeData.addItem(menuContainer.getParentMenu(), menuContainer);
        }
        dataProvider.refreshAll();
    }

    private TreeMenuContainer packContainer(TblMenu menu, boolean isParent) {
        TreeMenuContainer menuContainer = new TreeMenuContainer();
        menuContainer.setMenuId(menu.getMenuId());
        menuContainer.setMenuName(menu.getMenuName());
        // Default value 0
        menuContainer.setIsAdd("0");
        menuContainer.setIsUpdate("0");
        menuContainer.setIsView("0");
        menuContainer.setIsDelete("0");
        if (isParent) {
            menuContainer.setParentMenu(null);
        } else {
            menuContainer.setParentMenu(hMenu.get(menu.getParentId()));
        }

        return menuContainer;
    }

    @Override
    public void setModeNew() {
        txtGroupId.setEnabled(true);
        btnSave.setVisible(true);
    }

    @Override
    public void setModeUpdate() {
        txtGroupId.setEnabled(false);
        btnSave.setVisible(true);
    }

    @Override
    public void setModeView() {
        txtGroupId.setEnabled(false);
        btnSave.setVisible(false);
    }
}
