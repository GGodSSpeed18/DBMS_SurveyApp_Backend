package com.example.survey.data_transfer_objects;

import java.io.Serializable;

public class Option implements Serializable {
    private Long form_id;
    private Long question_id;
    private Long option_id;
    private Long option_int;
    private String option_string;
    private Float option_float;
    // private Boolean special_option;

    public Option() {
    }

    public Option(Float option_float) {
        this.option_float = option_float;
    }

    public Option(String option_string) {
        this.option_string = option_string;
    }

    public Option(Long option_int) {
        this.option_int = option_int;
    }

    public Long getForm_id() {
        return form_id;
    }

    public void setForm_id(Long form_id) {
        this.form_id = form_id;
    }

    public Long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    public Long getOption_id() {
        return option_id;
    }

    public void setOption_id(Long option_id) {
        this.option_id = option_id;
    }

    public Long getOption_int() {
        return option_int;
    }

    public void setOption_int(Long option_int) {
        this.option_int = option_int;
    }

    public String getOption_string() {
        return option_string;
    }

    public void setOption_string(String option_string) {
        this.option_string = option_string;
    }

    public Float getOption_float() {
        return option_float;
    }

    public void setOption_float(Float option_float) {
        this.option_float = option_float;
    }

    // public Boolean getSpecial_option() {
    // return special_option;
    // }

    // public void setSpecial_option(Boolean special_option) {
    // this.special_option = special_option;
    // }

}
