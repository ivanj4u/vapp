/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.framework.listener;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.TextField;

public abstract class FieldShortcutListener {

    public FieldShortcutListener() {
    }

    final ShortcutListener enterShortCut = new ShortcutListener(
            "EnterOnTextAreaShorcut", ShortcutAction.KeyCode.ENTER, null) {
        @Override
        public void handleAction(Object sender, Object target) {
            onEnterKeyPressed();
        }
    };

    public void install(final TextField textField) {
        textField.addFocusListener((FieldEvents.FocusListener) event -> {
            textField.addShortcutListener(enterShortCut);
        });

        textField.addBlurListener((FieldEvents.BlurListener) event -> {
            textField.removeShortcutListener(enterShortCut);
        });
    }

    public abstract void onEnterKeyPressed();
}
