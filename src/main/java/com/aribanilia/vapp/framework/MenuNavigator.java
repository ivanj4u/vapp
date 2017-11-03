/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.framework;

import com.aribanilia.vapp.entity.TblMenu;
import com.aribanilia.vapp.loader.MenuLoader;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
public class MenuNavigator extends Navigator {

    private MenuLoader menuLoader;

    @Autowired
    public MenuNavigator(final MenuLoader menuLoader, final ComponentContainer content, final SpringViewProvider viewProvider) {
        super(UI.getCurrent(), content);
        this.menuLoader = menuLoader;
        addProvider(viewProvider);
//        initViewProviders();
    }

//    private void initViewProviders() {
//        for (TblMenu menu: menuLoader.getAuthorizedMenu()) {
//            Class<? extends View> c = menuLoader.getView(menu);
//            if (c == null)
//                continue;
//            ViewProvider viewProvider = new ClassBasedViewProvider(menu.getMenuId(), c);
//            addProvider(viewProvider);
//        }
//    }
}
