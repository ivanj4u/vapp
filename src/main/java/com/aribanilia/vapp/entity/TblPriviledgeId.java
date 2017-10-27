/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vapp.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TblPriviledgeId implements Serializable {
    private long groupId;
    private String menuId;

    public TblPriviledgeId() {
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
