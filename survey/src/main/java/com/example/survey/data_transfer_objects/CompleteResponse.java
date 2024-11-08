package com.example.survey.data_transfer_objects;

import java.util.List;

public class CompleteResponse {
    private FormResponse metadata;
    private List<QuestionResponse> questions;

    public FormResponse getMetadata() {
        return metadata;
    }

    public void setMetadata(FormResponse metadata) {
        this.metadata = metadata;
    }

    public List<QuestionResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResponse> questions) {
        this.questions = questions;
    }

}
