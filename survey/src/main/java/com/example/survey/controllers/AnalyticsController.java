package com.example.survey.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.survey.analytics.AnalysisReturnObject;
import com.example.survey.data_transfer_objects.AnalyticFunctionRecord;
import com.example.survey.data_transfer_objects.QuestionDTO;
import com.example.survey.services.AnalyticService;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/analyse")
@RestController
public class AnalyticsController {
    @Autowired
    private AnalyticService analyticService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_MANAGE_FORMS','ROLE_AUTHOR_FORM')")
    public List<AnalyticFunctionRecord> getAllFunctions() {
        return analyticService.getAllFunctions();
    }

    @GetMapping("/for/{dtype}")
    public List<AnalyticFunctionRecord> getMethodName(@PathVariable int dtype) {
        return analyticService.getFunctionsForDataType(dtype);
    }

    @GetMapping("/{formid}/{questionid}")
    public List<AnalysisReturnObject> getQuestionAnalysis(@PathVariable long formid, @PathVariable long questionid) {
        return analyticService.getAnalysisForQuestion(formid, questionid);
    }

    @GetMapping("/{formid}")
    public List<QuestionDTO> getFormAnalysis(@PathVariable long formid) {
        return analyticService.getAnalysisForForm(formid);
    }

}
