package com.example.survey.data_transfer_objects;

public class QuestionResponse {
    private long user_id;
    private long question_id;
    private long form_id;
    private Long response_int;
    private String response_string;
    private Float response_float;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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

    public Long getResponse_int() {
        return response_int;
    }

    public void setResponse_int(Long response_int) {
        this.response_int = response_int;
    }

    public String getResponse_string() {
        return response_string;
    }

    public void setResponse_string(String response_string) {
        this.response_string = response_string;
    }

    public Float getResponse_float() {
        return response_float;
    }

    public void setResponse_float(Float response_float) {
        this.response_float = response_float;
    }

}
