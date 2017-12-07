package com.aribanilia.vaadin.view.param;

import com.aribanilia.vaadin.entity.TblParam;
import com.aribanilia.vaadin.framework.component.NotificationHelper;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.framework.impl.AbstractDetailScreen;
import com.aribanilia.vaadin.services.ParamServices;
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
public class DetailParamView extends AbstractDetailScreen {

    @Autowired private ParamServices servicesParam;

    private TextField txtKey, txtValue, txtDesc;
    private TblParam pojoParam;

    private static final Logger logger = LoggerFactory.getLogger(DetailParamView.class);

    @Override
    protected Component initDetail() {
        VerticalLayout layout = new VerticalLayout();
        layout.addStyleName(ValoTheme.PANEL_WELL);

        GridLayout grid = new GridLayout(2, 4);
        int row = 0;

        Label lbl = new Label("Id Param");
        lbl.setWidth("135px");

        grid.addComponent(lbl, 0, row);
        grid.addComponent(txtKey = new TextField(), 1, row++);

        grid.addComponent(new Label("Value Param"), 0, row);
        grid.addComponent(txtValue = new TextField(), 1, row++);

        grid.addComponent(new Label("Deskripsi"), 0, row);
        grid.addComponent(txtDesc = new TextField(), 1, row++);

        layout.addComponent(grid);
        return layout;
    }

    @Override
    protected void doSave() {
        if (!doValidate())
            return;

        try {
            if (getMode() == Constants.APP_MODE.MODE_NEW) {
                pojoParam = new TblParam();
                pojoParam.setKey(txtKey.getValue());
            }
            pojoParam.setValue(txtValue.getValue());
            pojoParam.setDescription(txtDesc.getValue());

            if (getMode() == Constants.APP_MODE.MODE_NEW) {
                servicesParam.save(pojoParam);
                listener.onAfterAdded(pojoParam);
            } else {
                servicesParam.update(pojoParam);
                listener.onAfterUpdated(pojoParam);
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
        if (ValidationHelper.validateRequired(txtKey) && ValidationHelper.validateRequired(txtValue)
                && ValidationHelper.validateRequired(txtDesc))
            return true;

        NotificationHelper.showNotification(Constants.APP_MESSAGE.WARN_DATA_MANDATORY);
        return false;
    }

    @Override
    protected void doReset() {
        txtKey.setValue("");
        txtDesc.setValue("");
        txtValue.setValue("");

        pojoParam = null;
    }

    @Override
    protected void setContentById(Object pojo) {
        pojoParam = pojo != null ? (TblParam) pojo : null;
        try {
            if (pojoParam != null) {
                txtKey.setValue(pojoParam.getKey());
                txtValue.setValue(pojoParam.getValue());
                txtDesc.setValue(pojoParam.getDescription());
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

        txtKey.setEnabled(true);
        txtValue.setEnabled(true);
        txtDesc.setEnabled(true);

        btnSave.setVisible(true);
    }

    @Override
    public void setModeUpdate() {
        txtKey.setEnabled(false);
        txtValue.setEnabled(true);
        txtDesc.setEnabled(true);

        btnSave.setVisible(true);
    }

    @Override
    public void setModeView() {
        txtKey.setEnabled(false);
        txtValue.setEnabled(false);
        txtDesc.setEnabled(false);

        btnSave.setVisible(false);
    }
}
