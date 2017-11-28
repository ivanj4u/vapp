/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.framework.component;

import com.aribanilia.vaadin.entity.TblMenu;
import com.aribanilia.vaadin.entity.TblUser;
import com.aribanilia.vaadin.framework.page.LoginPage;
import com.aribanilia.vaadin.framework.page.MainPage;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.loader.MenuLoader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@SuppressWarnings("deprecation")
@UIScope
public class MenuComponent extends CustomComponent {
    private MenuLoader menuLoader;

    // Component Menu
    private CssLayout menuItemsLayout;
    private MenuBar settings;

    public static final String ID = "menu";
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private static final Logger logger = LoggerFactory.getLogger(MenuComponent.class);

    @Autowired
    public MenuComponent(MenuLoader menuLoader) {
        this.menuLoader = menuLoader;

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

        if (settings == null) {
            settings = new MenuBar();
            settings.addStyleName("user-menu");
        }
        menuContent.addComponent(settings);

        menuContent.addComponent(buildToggleButton());

        if (menuItemsLayout == null) {
            menuItemsLayout = new CssLayout();
            menuItemsLayout.addStyleName("valo-menuitems");
        }
        menuContent.addComponent(menuItemsLayout);

        return menuContent;
    }

    private Component buildTitle() {
        Label logo = new Label("Spring <strong>Vaadin</strong>",
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

    private void buildUserMenu() {
        if (settings.getItems().size() == 0) {
            String imgSource = getCurrentUser().getImage() != null ? "img/" + getCurrentUser().getImage() : "img/guest.png";
            MenuBar.MenuItem settingsItem = settings.addItem("",
                    new ThemeResource(imgSource), null);
            settingsItem.addItem(Constants.CAPTION_MESSAGE.MENU_ITEM_PROFILE, menuItem ->  {
//                ProfilePreferencesWindow.open(user, false);
                Notification.show("Edit Profile Clicked");
            });
            settingsItem.addItem(Constants.CAPTION_MESSAGE.MENU_ITEM_PASSWORD, menuItem ->  {
//                ProfilePreferencesWindow.open(user, true);
                Notification.show("Preferences Clicked");
            });
            settingsItem.addSeparator();
            settingsItem.addItem(Constants.CAPTION_MESSAGE.MENU_ITEM_LOGOUT, menuItem ->  {
                doLogout();
            });
        }
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

    private void buildMenuItem() {
        if (menuItemsLayout.getComponentCount() == 0) {
            try {
                for (final TblMenu view : menuLoader.getAuthorizedMenu()) {
                    Button menuItemComponent = new Button();
                    menuItemComponent.setPrimaryStyleName(ValoTheme.MENU_ITEM);
                    menuItemComponent.setCaption(view.getMenuName().substring(0, 1).toUpperCase()
                            + view.getMenuName().substring(1));
                    menuItemComponent.addClickListener(event -> getUI().getNavigator().navigateTo(MainPage.VIEW_NAME + "/" + view.getMenuId()));
                    menuItemsLayout.addComponent(menuItemComponent);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }

    private void doLogout() {
        settings.removeItems();
        menuItemsLayout.removeAllComponents();
        VaadinSession.getCurrent().setAttribute(TblUser.class.getName(), null);
        getUI().getNavigator().navigateTo(LoginPage.VIEW_NAME);
    }

    @Override
    public void attach() {
        super.attach();
    }

    public void createMenu() {
        buildUserMenu();
        buildMenuItem();
    }

}
