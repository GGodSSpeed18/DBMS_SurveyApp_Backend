package com.example.survey.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.survey.data_access_layers.FormRepository;
import com.example.survey.data_transfer_objects.FormQuestions;
import com.example.survey.data_transfer_objects.UserDTO;
import com.example.survey.entities.Form;
import com.example.survey.entities.Group;
import com.example.survey.entities.User;

@Service
public class FormService {

    @Autowired
    private FormRepository formRepo;

    public List<Form> getFormsOfUser(long id) {
        return formRepo.getFormsOfUser(id);
    }

    public Form createForm(Form newform) {
        return formRepo.createForm(newform);
    }

    public Form createFullForm(FormQuestions formQuestions) {
        return formRepo.saveFormWithQuestions(formQuestions);
    }

    public void dropForm(long formid) {
        formRepo.dropForm(formid);
    }

    public List<Form> getAllForms() {
        return formRepo.getAllForms();
    }

    public List<Group> openToGroups(long id) {
        return formRepo.openToGroups(id);
    }

    public List<User> openToUsers(long formid, Boolean responded) {
        return formRepo.openToUsers(formid, responded);
    }

    public List<UserDTO> openToUsersDTO(long formid, Boolean responded) {
        return formRepo.openToUsersDTO(formid, responded);
    }

    public void addGrouptoForm(long id, long grpid) {
        formRepo.addGrouptoForm(id, grpid);
    }

    public Form getFormByID(long id) {
        return formRepo.getFormByID(id);
    }

    // public Form getFormByID(long id,boolean active) {
    // return formRepo.(id);
    // }

    public List<Form> filterActiveForms(List<Form> forms, boolean active) {
        List<Form> activeForms = new ArrayList<>();
        for (Form form : forms) {
            if (form.isActive() == active) {
                activeForms.add(form);
            }
        }
        return activeForms;
    }

    public Map<Long, Map<String, Object>> getAllFormResponses(long formid, boolean export) {
        return formRepo.getAllFormResponses(formid, export);
    }

    public void removeGroupFromForm(long formid, long grpid) {
        formRepo.removeGroupFromForm(formid, grpid);
    }

}
