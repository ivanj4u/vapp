/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.framework;

import com.aribanilia.vapp.loader.MenuLoader;
import com.aribanilia.vapp.service.SessionServices;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = MainPage.VIEW_NAME)
public class MainPage extends HorizontalLayout implements View {
    public static final String VIEW_NAME = "main";

    @Autowired private MenuLoader menuLoader;
    @Autowired private SessionServices servicesSession;
    @Autowired private SpringViewProvider viewProvider;

    @PostConstruct
    public void init() {
        setSizeFull();
        addStyleName("mainview");
        setSpacing(false);

        // Create Content
        final ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();

        final MenuComponent menuComponent = new MenuComponent(menuLoader, servicesSession, content);
        addComponent(menuComponent);
        addComponent(content);
        setExpandRatio(content, 1.0f);
    }

}
