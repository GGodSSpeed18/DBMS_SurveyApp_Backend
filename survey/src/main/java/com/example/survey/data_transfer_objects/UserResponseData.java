package com.example.survey.data_transfer_objects;

public class UserResponseData {
    private long question_id;
    private String prompt;
    private long user_id;
    private int data_type;
    private boolean uses_options;
    private String mapped_to;
    private Long response_int;
    private String response_string;
    private Float response_float;


    public UserResponseData() {

    }


    public UserResponseData(long question_id, String prompt, long user_id, int data_type, boolean uses_options,
            String mapped_to, Long response_int, String response_string, Float response_float) {
        this.question_id = question_id;
        this.prompt = prompt;
        this.user_id = user_id;
        this.data_type = data_type;
        this.uses_options = uses_options;
        this.mapped_to = mapped_to;
        this.response_int = response_int;
        this.response_string = response_string;
        this.response_float = response_float;
    }


    public long getQuestion_id() {
        return question_id;
    }


    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
    }


    public String getPrompt() {
        return prompt;
    }


    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }


    public long getUser_id() {
        return user_id;
    }


    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }


    public int getData_type() {
        return data_type;
    }


    public void setData_type(int data_type) {
        this.data_type = data_type;
    }


    public boolean isUses_options() {
        return uses_options;
    }


    public void setUses_options(boolean uses_options) {
        this.uses_options = uses_options;
    }


    public String getMapped_to() {
        return mapped_to;
    }


    public void setMapped_to(String mapped_to) {
        this.mapped_to = mapped_to;
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
