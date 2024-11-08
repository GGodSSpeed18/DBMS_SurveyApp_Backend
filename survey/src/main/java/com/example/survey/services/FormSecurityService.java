package com.example.survey.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.survey.data_access_layers.FormRepository;
import com.example.survey.data_access_layers.UserRepository;

@Service
public class FormSecurityService {
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private UserRepository userRepository;

    public Boolean isAuthor(String email, long formid) {
        return formRepository.isAuthor(email, formid);
    }

    public Boolean canRespond(String email, long formid) {
        return userRepository.canRespondToForm(email, formid);
    }
}
