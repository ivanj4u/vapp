/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.framework;

import com.aribanilia.vapp.loader.MenuLoader;
import com.aribanilia.vapp.service.SessionServices;
import com.aribanilia.vapp.view.LandingView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = MainPage.VIEW_NAME)
public class MainPage extends HorizontalLayout implements View {
    public static final String VIEW_NAME = "main";
    private static final Logger logger = LoggerFactory.getLogger(MainPage.class);

    @Autowired private MenuLoader menuLoader;
    @Autowired private SessionServices servicesSession;

    private Panel content;

    @PostConstruct
    public void init() {
        setSizeFull();
        addStyleName("mainview");
        setSpacing(false);

        // Create Content
        this.content = new Panel();
        content.addStyleName("view-content");
        content.setSizeFull();

        final MenuComponent menuComponent = new MenuComponent(menuLoader, servicesSession);
        addComponent(menuComponent);
        addComponent(content);
        setExpandRatio(content, 1.0f);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() == null || event.getParameters().isEmpty()) {
            logger.info("Event is null");
            LandingView view = new LandingView();
            view.show();
            content.setContent(view);
        } else {
            logger.info("Event is not null : " + event.getParameters());
            try {
//                content.removeAllComponents();
                AbstractScreen screen = menuLoader.getScreen(event.getParameters());
                screen.show();
                content.setContent(screen);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
