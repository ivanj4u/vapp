/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.framework.page;

import com.aribanilia.vaadin.entity.TblUser;
import com.aribanilia.vaadin.framework.component.MenuComponent;
import com.aribanilia.vaadin.framework.impl.AbstractScreen;
import com.aribanilia.vaadin.loader.MenuLoader;
import com.aribanilia.vaadin.services.SessionServices;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = MainPage.VIEW_NAME)
public class MainPage extends HorizontalLayout implements View {
    @Autowired private MenuLoader menuLoader;
    @Autowired private MenuComponent menuComponent;
    @Autowired private SessionServices servicesSession;
    @Autowired private LandingPage landingPage;

    public static final String VIEW_NAME = "main";
    private static final Logger logger = LoggerFactory.getLogger(MainPage.class);

    private Panel content;

    @PostConstruct
    public void init() {
        setSizeFull();
        addStyleName("mainview");
        setSpacing(false);

        // Create Content
        content = new Panel();
        content.addStyleName("view-content");
        content.setSizeFull();
        content.setContent(null);

        addComponent(menuComponent);
        addComponent(content);
        setExpandRatio(content, 1.0f);
    }

    private boolean validateUserSession() {
        try {
            TblUser user = VaadinSession.getCurrent().getAttribute(TblUser.class);
            if (user != null) {
                String sessionId = VaadinSession.getCurrent().getSession().getId();
                if (!servicesSession.sessionCheck(user.getUsername(), sessionId)) {
                    Notification.show("Anda telah keluar", "Anda Telah Keluar/Login dari Komputer Lain!", Notification.Type.HUMANIZED_MESSAGE);
                    VaadinSession.getCurrent().close();
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return true;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (!validateUserSession()) {
            return;
        }
        menuComponent.createMenu();
        if (event.getParameters() == null || event.getParameters().isEmpty()) {
            content.setContent(landingPage);
        } else {
            try {
                AbstractScreen screen = menuLoader.getScreen(event.getParameters());
                content.setContent(screen);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
