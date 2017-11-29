/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.framework.component;

import com.aribanilia.vaadin.entity.TblMenu;
import com.aribanilia.vaadin.entity.TblUser;
import com.aribanilia.vaadin.framework.constants.Constants;
import com.aribanilia.vaadin.framework.page.LoginPage;
import com.aribanilia.vaadin.framework.page.MainPage;
import com.aribanilia.vaadin.loader.MenuLoader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Hashtable;


@SuppressWarnings("deprecation")
@SpringComponent
@UIScope
public class MenuComponent extends CustomComponent {
    @Autowired
    private MenuLoader menuLoader;

    // Component Menu
    private CssLayout menuItemsLayout;
    private MenuBar settings;
    private Hashtable<String, MenuBar.MenuItem> hMenu;

    private static final String ID = "menu";
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private static final Logger logger = LoggerFactory.getLogger(MenuComponent.class);

    @PostConstruct
    public void init() {
        // Create Component
        hMenu = new Hashtable<>();
        settings = new MenuBar();
        settings.addStyleName("user-menu");

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

        menuContent.addComponent(settings);

        menuContent.addComponent(buildToggleButton());

        if (menuItemsLayout == null) {
            menuItemsLayout = new CssLayout();
//            menuItemsLayout.addStyleName(ValoTheme.MENU_ITEM);
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
        logoWrapper.addStyleName(ValoTheme.MENU_TITLE);
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
            settingsItem.addItem(Constants.CAPTION_MESSAGE.MENU_ITEM_LOGOUT, menuItem -> doLogout());
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
                hMenu.clear();
                for (final TblMenu view : menuLoader.getAuthorizedMenu()) {
                    MenuBar.MenuItem menuItem;
                    if (view.getHaveChild().equals("1")) {
                        if (view.getParentId().equals("0")) {
                            // Top Parent
                            MenuBar menuBar = new MenuBar();
                            menuBar.addStyleName(ValoTheme.MENU_ITEM);
                            menuBar.addStyleName(ValoTheme.PANEL_BORDERLESS);
                            menuBar.addStyleName(ValoTheme.MENUBAR_SMALL);
                            menuItemsLayout.addComponent(menuBar);
                            menuItem = menuBar.addItem(view.getMenuName(), null, null);
                        } else {
                            // Low Parent
                            menuItem = hMenu.get(view.getParentId()).addItem(view.getMenuName(), null, null);
                        }
                        hMenu.put(view.getMenuId(), menuItem);
                    } else {
                        menuItem = hMenu.get(view.getParentId());

                        String caption = view.getMenuName().substring(0, 1).toUpperCase()
                                + view.getMenuName().substring(1);
                        menuItem.addItem(caption, selectedItem -> getUI().getNavigator().navigateTo(MainPage.VIEW_NAME + "/" + view.getMenuId()));

                        /**
                         Button menuItemComponent = new Button();
                         menuItemComponent.setPrimaryStyleName(ValoTheme.MENU_ITEM);
                         menuItemComponent.setCaption(view.getMenuName().substring(0, 1).toUpperCase()
                         + view.getMenuName().substring(1));
                         menuItemComponent.addClickListener(event -> getUI().getNavigator().navigateTo(MainPage.VIEW_NAME + "/" + view.getMenuId()));
                         menuItemsLayout.addComponent(menuItemComponent);
                         */
                    }
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
