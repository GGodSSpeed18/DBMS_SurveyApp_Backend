package com.example.survey.entities;

import java.io.Serializable;

public class Role implements Serializable {
    private int role_id;
    private String role_name;
    private boolean can_respond;
    private boolean can_author_form;
    private boolean can_author_group;
    private boolean can_manage_forms;
    private boolean can_manage_groups;
    private boolean can_manage_roles;
    private boolean can_manage_users;

    public Role() {

    }

    public Role(int role_id, String role_name, boolean can_respond, boolean can_author_form, boolean can_author_group,
            boolean can_manage_forms, boolean can_manage_groups, boolean can_manage_roles, boolean can_manage_users) {
        this.role_id = role_id;
        this.role_name = role_name;
        this.can_respond = can_respond;
        this.can_author_form = can_author_form;
        this.can_author_group = can_author_group;
        this.can_manage_forms = can_manage_forms;
        this.can_manage_groups = can_manage_groups;
        this.can_manage_roles = can_manage_roles;
        this.can_manage_users = can_manage_users;
    }

    public boolean isCan_manage_groups() {
        return can_manage_groups;
    }

    public void setCan_manage_groups(boolean can_manage_groups) {
        this.can_manage_groups = can_manage_groups;
    }

    public boolean isCan_manage_roles() {
        return can_manage_roles;
    }

    public void setCan_manage_roles(boolean can_manage_roles) {
        this.can_manage_roles = can_manage_roles;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public boolean isCan_respond() {
        return can_respond;
    }

    public void setCan_respond(boolean can_respond) {
        this.can_respond = can_respond;
    }

    public boolean isCan_author_form() {
        return can_author_form;
    }

    public void setCan_author_form(boolean can_author_form) {
        this.can_author_form = can_author_form;
    }

    public boolean isCan_author_group() {
        return can_author_group;
    }

    public void setCan_author_group(boolean can_author_group) {
        this.can_author_group = can_author_group;
    }

    public boolean isCan_manage_forms() {
        return can_manage_forms;
    }

    public void setCan_manage_forms(boolean can_manage_forms) {
        this.can_manage_forms = can_manage_forms;
    }

    public boolean isCan_manage_users() {
        return can_manage_users;
    }

    public void setCan_manage_users(boolean can_manage_users) {
        this.can_manage_users = can_manage_users;
    }

}
