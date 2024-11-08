package com.example.survey.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.survey.data_access_layers.QuestionRepository;
import com.example.survey.data_transfer_objects.QuestionDTO;
import com.example.survey.entities.Question;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepo;

    public List<Question> getAllQuestions(long id) {
        return questionRepo.getAllQuestions(id);
    }

    public List<QuestionDTO> getAllQuestionsWithOptions(long formid) {
        return questionRepo.getAllQuestionsWithOptions(formid);
    }

    public void addQuesToForm(Question newQ, long id) {
        questionRepo.addQuesToForm(newQ, id);
    }

    public void addQuesWithOptionsToForm(QuestionDTO newQ, long formid) {
        questionRepo.addQuesWithOptionsToForm(newQ, formid);
    }

    public void dropQues(long id, long qid) {
        questionRepo.dropQues(id, qid);
    }

}
