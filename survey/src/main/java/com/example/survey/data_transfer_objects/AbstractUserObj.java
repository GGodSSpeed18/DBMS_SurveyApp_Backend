package com.example.survey.data_transfer_objects;

import com.example.survey.entities.User;

public class AbstractUserObj {
    private long user_id;
    private String email;
    private Integer user_role;
    private String name;

    public AbstractUserObj() {

    }

    public AbstractUserObj(User object) {
        this.user_id = object.getUser_id();
        this.email = object.getEmail();
        this.user_role = object.getUser_role();
        this.name = object.getFirst_name() + (object.getLast_name() != null ? (" " + object.getLast_name()) : "");
    }

    public AbstractUserObj(UserDTO object) {
        this.user_id = object.getUser_id();
        this.email = object.getEmail();
        this.user_role = object.getUser_role();
        this.name = object.getFirst_name() + (object.getLast_name() != null ? (" " + object.getLast_name()) : "");
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUser_role() {
        return user_role;
    }

    public void setUser_role(Integer user_role) {
        this.user_role = user_role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
