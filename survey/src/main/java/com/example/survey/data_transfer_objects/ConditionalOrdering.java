package com.example.survey.data_transfer_objects;

public class ConditionalOrdering {
    private long form_id;
    private long question_id;
    private long condition_id;
    private Integer compare_value_int;
    private String compare_value_string;
    private Float compare_value_float;
    private long question_default;
    private long question_alt;

    public long getForm_id() {
        return form_id;
    }

    public void setForm_id(long form_id) {
        this.form_id = form_id;
    }

    public long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
    }

    public long getCondition_id() {
        return condition_id;
    }

    public void setCondition_id(long condition_id) {
        this.condition_id = condition_id;
    }

    public Integer getCompare_value_int() {
        return compare_value_int;
    }

    public void setCompare_value_int(Integer compare_value_int) {
        this.compare_value_int = compare_value_int;
    }

    public String getCompare_value_string() {
        return compare_value_string;
    }

    public void setCompare_value_string(String compare_value_string) {
        this.compare_value_string = compare_value_string;
    }

    public Float getCompare_value_float() {
        return compare_value_float;
    }

    public void setCompare_value_float(Float compare_value_float) {
        this.compare_value_float = compare_value_float;
    }

    public long getQuestion_default() {
        return question_default;
    }

    public void setQuestion_default(long question_default) {
        this.question_default = question_default;
    }

    public long getQuestion_alt() {
        return question_alt;
    }

    public void setQuestion_alt(long question_alt) {
        this.question_alt = question_alt;
    }

}
