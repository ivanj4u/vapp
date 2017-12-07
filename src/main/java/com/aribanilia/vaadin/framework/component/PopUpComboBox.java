/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.framework.component;

import com.vaadin.ui.ComboBox;

import java.util.HashMap;

public class PopUpComboBox extends ComboBox<ItemComponent> {

    private final HashMap<Object, ItemComponent> h;

    public PopUpComboBox() {
        super();
        this.h = new HashMap<>();
        setEmptySelectionCaption("Pilih data");
        setEmptySelectionAllowed(false);
        setItemCaptionGenerator(ItemComponent::getCaption);
    }

    public void addItem(ItemComponent item) {
        h.put(item.getValue(), item);
        super.setItems(h.values());
    }

    public void setValueItem(Object value) {
        if (h.get(value) != null) {
            setValue(h.get(value));
        } else {
            setValue((ItemComponent) h.values().toArray()[0]);
        }
    }

    public Object getValueItem() {
        ItemComponent itemComponent = getValue();
        if (itemComponent != null) {
            return itemComponent.getValue();
        }
        return null;
    }

    @Override
    public void clear() {
        super.clear();
        h.clear();
    }
}
