package com.example.survey.data_transfer_objects;

import java.util.List;

import com.example.survey.analytics.AnalysisReturnObject;
import com.example.survey.entities.DataType;

public class QuestionDTO {
    private long form_id;
    private Long question_id;
    private int data_type;
    private boolean required;
    private String prompt;
    private Long min_val;
    private Long max_val;
    private boolean conditional;
    private List<Option> options;
    private DataType dataType;
    private List<AnalysisReturnObject> analysisReturnObjects;

    public QuestionDTO() {
    }

    public long getForm_id() {
        return form_id;
    }

    public void setForm_id(long form_id) {
        this.form_id = form_id;
    }

    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    public long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
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

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public List<AnalysisReturnObject> getAnalysisReturnObjects() {
        return analysisReturnObjects;
    }

    public void setAnalysisReturnObjects(List<AnalysisReturnObject> analysisReturnObjects) {
        this.analysisReturnObjects = analysisReturnObjects;
    }

}
