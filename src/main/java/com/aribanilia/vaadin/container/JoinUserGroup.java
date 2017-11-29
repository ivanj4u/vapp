/*
 * Copyright (c) 2017.
 */

package com.aribanilia.vaadin.container;

import com.aribanilia.vaadin.entity.*;

import java.io.Serializable;

public class JoinUserGroup implements Serializable {
    // Join TblGroup
    private TblGroup group;
    private long group_groupId;
    private String group_groupName;
    // Join TblUser
    private TblUser user;
    private String user_username, user_name;
    // Join TblUserGroup
    private TblUserGroup userGroup;
    private long userGroup_groupId;
    private String userGroup_username;

    public JoinUserGroup(TblGroup group, TblUser user, TblUserGroup userGroup) {
        // Set TblGroup
        this.group = group;
        setGroup_groupId(group.getGroupId());
        setGroup_groupName(group.getGroupName());
        // Set TblUser
        this.user = user;
        setUser_name(user.getName());
        setUser_username(user.getUsername());
        // Set TblUserGroup
        this.userGroup = userGroup;
        setUserGroup_groupId(userGroup.getGroupId());
        setUserGroup_username(userGroup.getUsername());
    }

    public long getGroup_groupId() {
        return group_groupId;
    }

    public void setGroup_groupId(long group_groupId) {
        this.group_groupId = group_groupId;
    }

    public String getGroup_groupName() {
        return group_groupName;
    }

    public void setGroup_groupName(String group_groupName) {
        this.group_groupName = group_groupName;
    }

    public String getUser_username() {
        return user_username;
    }

    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public long getUserGroup_groupId() {
        return userGroup_groupId;
    }

    public void setUserGroup_groupId(long userGroup_groupId) {
        this.userGroup_groupId = userGroup_groupId;
    }

    public String getUserGroup_username() {
        return userGroup_username;
    }

    public void setUserGroup_username(String userGroup_username) {
        this.userGroup_username = userGroup_username;
    }
}
