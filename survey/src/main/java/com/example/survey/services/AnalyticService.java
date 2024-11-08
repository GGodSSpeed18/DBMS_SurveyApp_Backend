package com.example.survey.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.survey.analytics.AnalysisReturnObject;
import com.example.survey.data_access_layers.AnalyticFunctionRepository;
import com.example.survey.data_access_layers.QuestionRepository;
import com.example.survey.data_transfer_objects.AnalyticFunctionRecord;
import com.example.survey.data_transfer_objects.QuestionDTO;
import com.example.survey.entities.DataType;

@Service
public class AnalyticService {
    @Autowired
    private AnalyticFunctionRepository analyticFunctionRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public List<AnalyticFunctionRecord> getAllFunctions() {
        return analyticFunctionRepository.getAllFunctions();
    }

    public List<AnalyticFunctionRecord> getFunctionsForDataType(int data_id) {
        return analyticFunctionRepository.getFunctionsForDataType(data_id);
    }

    public List<AnalyticFunctionRecord> getFunctionsForDataType(DataType dataType) {
        return getFunctionsForDataType(dataType.getType_id());
    }

    public List<AnalysisReturnObject> getAnalysisForList(int data_id, List<?> list) {
        return analyticFunctionRepository.getAnalysisForList(data_id, list);
    }

    public List<AnalysisReturnObject> getAnalysisForQuestion(long form_id, long question_id) {
        return analyticFunctionRepository.getAnalysisForQuestion(form_id, question_id);
    }

    public List<QuestionDTO> getAnalysisForForm(long form_id) {
        List<QuestionDTO> questions = questionRepository.getAllQuestionsWithOptions(form_id);
        for (QuestionDTO question : questions) {
            question.setAnalysisReturnObjects(analyticFunctionRepository.getAnalysisForQuestion(question));
        }
        return questions;
    }
}
