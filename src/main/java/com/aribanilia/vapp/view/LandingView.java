/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.view;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

@SpringView
public class LandingView extends VerticalLayout implements View {

    @PostConstruct
    public void init() {
        addComponent(new Label("Selamat Datang di Aplikasi Vaadin"));
    }
}
