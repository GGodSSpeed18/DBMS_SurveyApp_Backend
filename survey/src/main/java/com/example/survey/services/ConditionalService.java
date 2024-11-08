package com.example.survey.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.survey.data_access_layers.ConditionalRepository;
import com.example.survey.data_transfer_objects.ConditionalOrdering;

@Service
public class ConditionalService {
    @Autowired
    private ConditionalRepository conditionalRepository;

    public List<ConditionalOrdering> getFormOrderings(long form_id) {
        return conditionalRepository.getFormOrderings(form_id);
    }

    public ConditionalOrdering getOrdering(long form_id, long question_id, long condition_id) {
        return conditionalRepository.getOrdering(form_id, question_id, condition_id);

    }

    public int isConditional(long form_id, long question_id) {
        return conditionalRepository.isConditional(form_id, question_id);

    }

}
