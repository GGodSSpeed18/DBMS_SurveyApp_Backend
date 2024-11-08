package com.example.survey.data_transfer_objects;

import java.util.List;

import com.example.survey.entities.Form;

public class FormQuestions {
    Form form;
    List<QuestionDTO> questions;
    List<ConditionalOrdering> conditional_orderings;

    public FormQuestions(Form form, List<QuestionDTO> questions, List<ConditionalOrdering> conditionalOrderings) {
        this.form = form;
        this.questions = questions;
        this.conditional_orderings = conditionalOrderings;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public List<ConditionalOrdering> getConditional_orderings() {
        return conditional_orderings;
    }

    public void setConditional_orderings(List<ConditionalOrdering> conditionalOrderings) {
        this.conditional_orderings = conditionalOrderings;
    }

}
