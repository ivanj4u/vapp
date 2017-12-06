/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.view.user;

import com.aribanilia.vaadin.container.TreeMenuContainer;
import com.aribanilia.vaadin.framework.component.ItemComponent;
import com.aribanilia.vaadin.framework.impl.AbstractScreen;
import com.aribanilia.vaadin.framework.listener.CallbackListener;
import com.aribanilia.vaadin.util.ValidationHelper;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

@UIScope
public class DetailMenuContainerView extends AbstractScreen {

    private ComboBox<ItemComponent> cmbAdd, cmbEdit, cmbView, cmbDelete;
    private Button btnDone;
    private CallbackListener listener;
    private TreeMenuContainer menuContainer;

    public DetailMenuContainerView(TreeMenuContainer menuContainer, CallbackListener listener) {
        super();
        this.listener = listener;
        this.menuContainer = menuContainer;
    }

    @Override
    protected void initComponents() {
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(initDetail());
    }

    private Component initDetail() {
        VerticalLayout layout = new VerticalLayout();
        GridLayout grid = new GridLayout(2, 5);
        int row = 0;
        Label lbl = new Label("Priviledge Tambah");
        lbl.setWidth("115px");
        grid.addComponent(lbl, 0, row);
        grid.addComponent(cmbAdd = new ComboBox<>(), 1, row++);
        createComboBoxData(cmbAdd);
        grid.addComponent(new Label("Priviledge Ubah"), 0, row);
        grid.addComponent(cmbEdit = new ComboBox<>(), 1, row++);
        createComboBoxData(cmbEdit);
        grid.addComponent(new Label("Priviledge Ubah"), 0, row);
        grid.addComponent(cmbView = new ComboBox<>(), 1, row++);
        createComboBoxData(cmbView);
        grid.addComponent(new Label("Priviledge Ubah"), 0, row);
        grid.addComponent(cmbDelete = new ComboBox<>(), 1, row++);
        createComboBoxData(cmbDelete);
        grid.addComponent(btnDone = new Button("Selesai"), 1, row++);
        btnDone.addClickListener(event -> doCallback());

        layout.addComponent(grid);
        return layout;
    }

    private void createComboBoxData(ComboBox<ItemComponent> cmb) {
        ItemComponent itemNo = new ItemComponent("0", "Tidak");
        ItemComponent itemYes = new ItemComponent("1", "Iya");
        cmb.setItems(itemNo, itemYes);
    }

    private void doCallback() {
        if (ValidationHelper.validateValueNotNull(cmbAdd.getValue().getValue())
                && ValidationHelper.validateValueNotNull(cmbEdit.getValue().getValue())
                && ValidationHelper.validateValueNotNull(cmbView.getValue().getValue())
                && ValidationHelper.validateValueNotNull(cmbDelete.getValue().getValue())) {
        }
        if (listener != null) {
            menuContainer.setIsAdd(cmbAdd.getValue().getValue().toString());
            menuContainer.setIsUpdate(cmbEdit.getValue().getValue().toString());
            menuContainer.setIsView(cmbView.getValue().getValue().toString());
            menuContainer.setIsDelete(cmbDelete.getValue().getValue().toString());
            listener.onCallback(menuContainer);
        }
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
