/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.framework;

import com.aribanilia.vapp.entity.TblUser;
import com.aribanilia.vapp.model.LandingPage;
import com.aribanilia.vapp.model.LoginPage;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme(ValoTheme.THEME_NAME)
@Title("Vaadin Spring")
public class VaadinUI extends UI {
    @Autowired private SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest request) {
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        // Initialize Navigator
        Navigator navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);
        navigator.setErrorView(LoginPage.class);

        updateContent();
    }

    private void updateContent() {
        if (getSession().getAttribute(TblUser.class.getName()) != null) {
            // Authenticated user
//            removeStyleName("loginview");
            getNavigator().navigateTo(LandingPage.VIEW_NAME);
        } else {
            getNavigator().navigateTo(LoginPage.VIEW_NAME);
//            addStyleName("loginview");
        }
    }
}
