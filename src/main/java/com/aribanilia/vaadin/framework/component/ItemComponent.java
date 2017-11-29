/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.framework.component;

public class ItemComponent {
    private String caption;
    private Object value;

    public ItemComponent(Object value, String caption) {
        this.caption = caption;
        this.value = value;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
