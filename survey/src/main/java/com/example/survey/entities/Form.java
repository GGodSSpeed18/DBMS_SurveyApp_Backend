package com.example.survey.entities;

import java.io.Serializable;
import java.util.Date;

public class Form implements Serializable {
    private long form_id;
    private String form_name;
    private long author;
    private Date created_at;
    private String form_desc;
    private Date start_time;
    private Date end_time;

    public Form() {

    }

    public Form(long form_id, String form_name, long author, String form_desc, Date start_time,
            Date end_time) {
        this.form_id = form_id;
        this.form_name = form_name;
        this.author = author;
        this.created_at = new Date();
        this.form_desc = form_desc;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public long getForm_id() {
        return form_id;
    }

    public void setForm_id(long form_id) {
        this.form_id = form_id;
    }

    public String getForm_name() {
        return form_name;
    }

    public void setForm_name(String form_name) {
        this.form_name = form_name;
    }

    public long getAuthor() {
        return author;
    }

    public void setAuthor(long author) {
        this.author = author;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getForm_desc() {
        return form_desc;
    }

    public void setForm_desc(String form_desc) {
        this.form_desc = form_desc;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public boolean isActive() {
        Date currenDate = new Date();
        return (((end_time != null) ? currenDate.before(end_time) : true) && currenDate.after(start_time));
    }
}
