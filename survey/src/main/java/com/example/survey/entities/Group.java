package com.example.survey.entities;

import java.io.Serializable;
import java.util.Date;

public class Group implements Serializable{
    private long group_id;
    private String group_name;
    private Date created_at;
    private Long group_owner;

    public Group(){
        // this.created_at = null;
    }

    public Group(long group_id, String group_name, Long group_owner) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.created_at = new Date();
        this.group_owner = group_owner;
    }

    public Date getCreated_at() {
        return created_at;
    }
    // ? Wanted to make it immutable but querying fails if setter is not provided!!
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Long getGroup_owner() {
        return group_owner;
    }

    public void setGroup_owner(Long group_owner) {
        this.group_owner = group_owner;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
