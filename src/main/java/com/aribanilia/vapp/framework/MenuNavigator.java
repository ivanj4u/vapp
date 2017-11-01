/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.framework;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

@UIScope
public class MenuNavigator extends Navigator {
    public MenuNavigator(SpringViewProvider viewProvider, ViewDisplay content) {
        super(UI.getCurrent(), content);
        addProvider(viewProvider);
    }
}
