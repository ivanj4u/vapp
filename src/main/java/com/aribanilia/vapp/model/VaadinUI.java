/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.model;

import com.aribanilia.vapp.entity.TblUser;
import com.aribanilia.vapp.loader.MenuLoader;
import com.aribanilia.vapp.service.MenuServices;
import com.aribanilia.vapp.service.UserServices;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme(ValoTheme.THEME_NAME)
@Title("Vaadin Spring")
public class VaadinUI extends UI {

    @Autowired private UserServices servicesUser;
    @Autowired private MenuServices servicesMenu;

    private Navigator navigator;

    @Override
    protected void init(VaadinRequest request) {
        Responsive.makeResponsive(this);

        // Initializer Menu
        MenuLoader menuLoader = new MenuLoader(servicesUser, servicesMenu);

        // Initializer All UI
        navigator = new Navigator(this, this);

        LoginPage login = new LoginPage(servicesUser);
        navigator.addView(LoginPage.VIEW_NAME, login);
        navigator.setErrorView(LoginPage.class);

        updateContent();
    }

    private void updateContent() {
        if (getSession().getAttribute(TblUser.class.getName()) != null) {
            // Authenticated user
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            getNavigator().navigateTo(LoginPage.VIEW_NAME);
            addStyleName("loginview");
        }
    }
}
