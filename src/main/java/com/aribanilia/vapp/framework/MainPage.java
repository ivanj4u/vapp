/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.framework;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = MainPage.VIEW_NAME)
public class MainPage extends HorizontalLayout implements View {
    public static final String VIEW_NAME = "main";

    @Autowired private MenuComponent menuComponent;

    @PostConstruct
    public void init() {
        setSizeFull();
        addStyleName("mainview");
        setSpacing(false);
        addComponent(menuComponent);

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();

        addComponent(content);
        setExpandRatio(content, 1.0f);
    }
}
