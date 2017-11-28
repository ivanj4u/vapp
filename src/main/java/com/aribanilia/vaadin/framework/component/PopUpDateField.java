/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.framework.component;

import com.vaadin.ui.DateField;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class PopUpDateField extends DateField {

    public PopUpDateField() {
        super();
        setDateFormat("dd-MM-yyyy");
    }

    public void setValueDate(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            setValue(cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            setValue(null);
        }
    }

    public Date getValueDate() {
        if (getValue() != null) {
            Date date = Date.from(getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            return date;

        }
        return null;
    }
}
