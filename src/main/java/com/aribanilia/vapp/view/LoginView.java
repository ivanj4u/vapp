/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.view;

import com.aribanilia.vapp.entity.TblUser;
import com.aribanilia.vapp.loader.MenuLoader;
import com.aribanilia.vapp.model.LandingPage;
import com.aribanilia.vapp.service.UserServices;
import com.aribanilia.vapp.util.VConstants;
import com.aribanilia.vapp.util.VaadinValidation;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = LoginView.VIEW_NAME)
public class LoginView extends VerticalLayout implements View {
    @Autowired private UserServices servicesUser;
    @Autowired private MenuLoader menuLoader;

    public static final String VIEW_NAME = "login";
    private static final Logger logger = LoggerFactory.getLogger(LoginView.class);

    @PostConstruct
    private void init() {
        setSizeFull();
        setMargin(false);
        setSpacing(true);
        Responsive.makeResponsive(this);

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

        Notification notification = new Notification(
                "Selamat Datang ke Aplikasi Vaadin Spring");
        notification
                .setDescription("<span>Untuk mendapatkan <b>Username</b> & <b>Password</b> dapat menghubungi : <a href=\"https://twitter.com/ivan_j4u\">Ivan Aribanilia</a> .</span>" +
                        "<span> Terima kasih </span>");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(20000);
        notification.show(UI.getCurrent().getPage());
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setMargin(false);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        return loginPanel;
    }

    private Component buildFields() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addStyleName("fields");

        final TextField txtUsername = new TextField("Username");
        txtUsername.setIcon(FontAwesome.USER);
        txtUsername.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField txtPassword = new PasswordField("Password");
        txtPassword.setIcon(FontAwesome.LOCK);
        txtPassword.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button btnLogin = new Button("Login", event -> {
            if (VaadinValidation.validateRequired(txtUsername)
                    && VaadinValidation.validateRequired(txtPassword)) {
                TblUser user = servicesUser.login(txtUsername.getValue(), txtPassword.getValue());
                if (user != null) {
                    if (VConstants.STATUS_USER.ACTIVE.equals(user.getStatus())) {
                        VaadinSession.getCurrent().setAttribute(TblUser.class, user);
                        menuLoader.setAuthorizedMenu(user);
                        getUI().getNavigator().navigateTo(LandingPage.VIEW_NAME);
                    } else {
                        logger.error("Status User : " + user.getUsername() + " tidak benar!");
                        Notification.show("Status User : " + user.getUsername() + " tidak benar!", Notification.Type.ERROR_MESSAGE);
                    }
                } else {
                    logger.error("User : " + txtUsername.getValue() + " tidak ditemukan!");
                    Notification.show("User : " + txtUsername.getValue() + " tidak ditemukan!", Notification.Type.ERROR_MESSAGE);
                }
            }
        });
        btnLogin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnLogin.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        btnLogin.focus();

        layout.addComponents(txtUsername, txtPassword, btnLogin);
        layout.setComponentAlignment(btnLogin, Alignment.BOTTOM_LEFT);

        return layout;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Selamat Datang");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        return labels;
    }
}
