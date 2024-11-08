package com.example.survey.entities;

import java.io.Serializable;

public class Question implements Serializable{
    private long question_id;
    private long form_id;
    private int data_type;
    private boolean required;
    private String prompt;
    private Long min_val;
    private Long max_val;
    private boolean conditional;

    public Question() {

    }

    public Question(long question_id, long form_id, int data_type, boolean required, String prompt, Long min_val,
            Long max_val, boolean conditional) {
        this.question_id = question_id;
        this.form_id = form_id;
        this.data_type = data_type;
        this.required = required;
        this.prompt = prompt;
        this.min_val = min_val;
        this.max_val = max_val;
        this.conditional = conditional;
    }

    public long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
    }

    public long getForm_id() {
        return form_id;
    }

    public void setForm_id(long form_id) {
        this.form_id = form_id;
    }

    public int getData_type() {
        return data_type;
    }

    public void setData_type(int data_type) {
        this.data_type = data_type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Long getMin_val() {
        return min_val;
    }

    public void setMin_val(Long min_val) {
        this.min_val = min_val;
    }

    public Long getMax_val() {
        return max_val;
    }

    public void setMax_val(Long max_val) {
        this.max_val = max_val;
    }

    public boolean isConditional() {
        return conditional;
    }

    public void setConditional(boolean conditional) {
        this.conditional = conditional;
    }
    
}
