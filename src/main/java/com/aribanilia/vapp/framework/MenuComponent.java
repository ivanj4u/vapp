/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.framework;

import com.aribanilia.vapp.entity.TblMenu;
import com.aribanilia.vapp.entity.TblUser;
import com.aribanilia.vapp.loader.MenuLoader;
import com.aribanilia.vapp.service.SessionServices;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;


@SpringComponent
@UIScope
public class MenuComponent extends CustomComponent {
    @Autowired private MenuLoader menuLoader;
    @Autowired private SessionServices servicesSession;
    @Autowired private SpringViewProvider viewProvider;

    private MenuBar.MenuItem settingsItem;
    private MenuNavigator menuNavigator;

    public static final String ID = "menu";
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private static final Logger logger = LoggerFactory.getLogger(MenuComponent.class);

    @PostConstruct
    public void init() {
        if (getCurrentUser() != null) {
            setPrimaryStyleName("valo-menu");
            setId(ID);
            setSizeUndefined();

            setCompositionRoot(buildContent());
        }
    }

    private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildUserMenu());
        menuContent.addComponent(buildToggleButton());
        menuContent.addComponent(buildMenuItem());

        return menuContent;
    }

    private Component buildTitle() {
        Label logo = new Label("QuickTickets <strong>Dashboard</strong>",
                ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        logoWrapper.setSpacing(false);
        return logoWrapper;
    }

    private TblUser getCurrentUser() {
        return VaadinSession.getCurrent()
                .getAttribute(TblUser.class);
    }

    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
        final TblUser user = getCurrentUser();
        settingsItem = settings.addItem("",
                new ThemeResource("img/logo.jpg"), null);
        settingsItem.addItem("Edit Profile", menuItem ->  {
//                ProfilePreferencesWindow.open(user, false);
            Notification.show("Edit Profile Clicked");
        });
        settingsItem.addItem("Preferences", menuItem ->  {
//                ProfilePreferencesWindow.open(user, true);
            Notification.show("Preferences Clicked");
        });
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", menuItem ->  {
            VaadinSession.getCurrent().setAttribute(TblUser.class.getName(), null);
            getUI().getNavigator().navigateTo(LoginPage.VIEW_NAME);
        });
        return settings;
    }

    private Component buildToggleButton() {
        Button valoMenuToggleButton = new Button("Menu", event -> {
            if (getCompositionRoot().getStyleName()
                    .contains(STYLE_VISIBLE)) {
                getCompositionRoot().removeStyleName(STYLE_VISIBLE);
            } else {
                getCompositionRoot().addStyleName(STYLE_VISIBLE);
            }
        });
        valoMenuToggleButton.setIcon(FontAwesome.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return valoMenuToggleButton;
    }

    private Component buildMenuItem() {
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");
        try {
            if (!validateUserSession()) {
                return null;
            }
            for (final TblMenu view : menuLoader.getAuthorizedMenu()) {
                AbstractScreen screen = menuLoader.getScreen(view.getMenuId());
                if (screen == null) {
                    logger.error("Screen Error : " + view.getMenuClass());
                    continue;
                }
                Button menuItemComponent = new Button();
                menuItemComponent.setPrimaryStyleName(ValoTheme.MENU_ITEM);
                menuItemComponent.setCaption(view.getMenuName().substring(0, 1).toUpperCase()
                        + view.getMenuName().substring(1));
                menuItemComponent.addClickListener(event -> {
                    getUI().getNavigator().navigateTo(view.getMenuId());
                });
                menuItemsLayout.addComponent(menuItemComponent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return menuItemsLayout;
    }

    private boolean validateUserSession() throws Exception {
        TblUser user = VaadinSession.getCurrent().getAttribute(TblUser.class);
        String sessionId = VaadinSession.getCurrent().getSession().getId();
        if (!servicesSession.sessionCheck(user.getUsername(), sessionId)) {
            Notification.show("Anda telah keluar", "Anda Telah Keluar/Login dari Komputer Lain!", Notification.Type.HUMANIZED_MESSAGE);
            VaadinSession.getCurrent().close();
            return false;
        }
        return true;
    }

    @Override
    public void attach() {
        super.attach();
    }

}
