/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.framework.impl;

public interface PriviledgeModel {
    boolean setMode(int mode);

    boolean isAuthorizedToView();

    boolean isAuthorizedToUpdate();

    boolean isAuthorizedToDelete();

    boolean isAuthorizedToAdd();
}
