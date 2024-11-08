package com.example.survey.data_transfer_objects;

import java.sql.Date;

import com.example.survey.entities.Role;
import com.example.survey.entities.User;

public class UserDTO {
    private long user_id;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String email;
    private Date dob;
    private String gender;
    private Integer user_role;

    // Extra attribute
    private Role role;

    public UserDTO() {
    }

    public UserDTO(long user_id, String first_name, String middle_name, String last_name, String email, Date dob,
            String gender, Integer user_role) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.user_role = user_role;
    }

    public UserDTO(User user) {
        this.user_id = user.getUser_id();
        this.first_name = user.getFirst_name();
        this.middle_name = user.getMiddle_name();
        this.last_name = user.getLast_name();
        this.email = user.getEmail();
        this.dob = user.getDob();
        this.gender = user.getGender();
        this.user_role = user.getUser_role();
        this.role = user.getRole();
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getUser_role() {
        return user_role;
    }

    public void setUser_role(Integer user_role) {
        this.user_role = user_role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
