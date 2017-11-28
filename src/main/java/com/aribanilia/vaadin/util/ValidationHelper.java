/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.util;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Notification;

public class ValidationHelper {

    public static boolean validateRequired(AbstractField field) {
        if (field.getValue() == null || field.getValue().toString().equals("")) {
            Notification.show("Mohon mengisi field : " + field.getCaption(), Notification.Type.HUMANIZED_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean validateFieldWithoutWarn(AbstractField field) {
        if (field.getValue() == null || field.getValue().toString().equals("")) {
            return false;
        }
        return true;
    }

    public static boolean validateValueNotNull(Object value) {
        if (value != null && !value.toString().equals(""))
            return true;
        return false;
    }
}
