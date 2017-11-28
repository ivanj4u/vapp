/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.framework;

import com.aribanilia.vaadin.entity.TblUser;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme(ValoTheme.THEME_NAME)
@Title("Vaadin Spring")

public class VaadinUI extends UI {
    @Autowired private SpringViewProvider viewProvider;
    private MainPage mainPage;

    private static final Logger logger = LoggerFactory.getLogger(VaadinUI.class);

    @Override
    protected void init(VaadinRequest request) {
        mainPage = new MainPage();

        setErrorHandler(event -> {
            Throwable t = DefaultErrorHandler.findRelevantThrowable(event.getThrowable());
            logger.error("Error during request", t);
        });

        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        // Initialize Navigator
        Navigator navigator = new Navigator(this,this);
        navigator.addProvider(viewProvider);
        navigator.setErrorView(LoginPage.class);
        setNavigator(navigator);

        updateContent();
    }

    private void updateContent() {
        if (getSession().getAttribute(TblUser.class.getName()) != null) {
            setContent(mainPage);
        } else {
            getNavigator().navigateTo(LoginPage.VIEW_NAME);
        }
    }
}
