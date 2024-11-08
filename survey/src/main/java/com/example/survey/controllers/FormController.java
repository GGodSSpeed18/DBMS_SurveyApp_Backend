package com.example.survey.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.survey.data_access_layers.ResourceAvailabilityRepository;
import com.example.survey.data_transfer_objects.AbstractUserObj;
import com.example.survey.data_transfer_objects.ConditionalOrdering;
import com.example.survey.data_transfer_objects.FormQuestions;
import com.example.survey.data_transfer_objects.QuestionDTO;
import com.example.survey.data_transfer_objects.UserResponseData;
import com.example.survey.entities.Form;
import com.example.survey.entities.Group;
import com.example.survey.entities.User;
import com.example.survey.services.ConditionalService;
import com.example.survey.services.FormSecurityService;
import com.example.survey.services.FormService;
import com.example.survey.services.NotificationService;
import com.example.survey.services.QuestionService;

@RestController
public class FormController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private FormService formService;
    @Autowired
    private QuestionService questionService;
    @SuppressWarnings("unused")
    @Autowired
    private FormSecurityService formSecurityService;
    @SuppressWarnings("unused")
    @Autowired
    private FormSecurityService userSecurityService;
    @Autowired
    private ResourceAvailabilityRepository resourceAvailabilityRepository;
    @Autowired
    private ConditionalService conditionalService;

    @GetMapping("/forms/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') || @formSecurityService.isAuthor(authentication.name,#id)")
    public Form getFormByID(@PathVariable long id) {
        return formService.getFormByID(id);
    }

    @GetMapping("/forms/user/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') || (hasRole('ROLE_AUTHOR_FORM') && @userSecurityService.matchEmailToId(#id,authentication.name))")
    public List<Form> getFormsOfUser(@PathVariable long id) {
        return formService.getFormsOfUser(id);
    }

    @GetMapping("/forms/user/me")
    @PreAuthorize("hasRole('ROLE_AUTHOR_FORM')")
    public List<Form> getMyForms() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return formService.getFormsOfUser(((User) authentication.getPrincipal()).getUser_id());
    }

    @GetMapping("/forms")
    @PreAuthorize("hasAnyRole('ROLE_MANAGE_FORMS','ROLE_AUTHOR_FORM')")
    public List<Form> getAllForms() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (!user.getRole().isCan_manage_forms()) {
            return formService.getFormsOfUser(user.getUser_id());
        }
        return formService.getAllForms();
    }

    @GetMapping("/forms/{id}/groups")
    @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') || @formSecurityService.isAuthor(authentication.name,#id)")
    public List<Group> openToGroups(@PathVariable long id) {
        return formService.openToGroups(id);
    }

    @PostMapping("/forms")
    @PreAuthorize("hasRole('ROLE_AUTHOR_FORM')")
    public Form createForm(@RequestBody Form newform) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        newform.setAuthor(currentUser.getUser_id());
        return formService.createForm(newform);
    }

    @PostMapping("/forms/complete")
    @PreAuthorize("hasRole('ROLE_AUTHOR_FORM')")
    public Form createFullForm(@RequestBody FormQuestions formQuestions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        formQuestions.getForm().setAuthor(currentUser.getUser_id());
        return formService.createFullForm(formQuestions);
    }

    @PostMapping("/forms/{id}/addgroup")
    @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') || @formSecurityService.isAuthor(authentication.name,#id)")
    public void addGrouptoForm(@PathVariable long id, @RequestParam long grpid) {
        formService.addGrouptoForm(id, grpid);
    }

    @DeleteMapping("/forms/{id}/dropgroup")
    @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') || @formSecurityService.isAuthor(authentication.name,#id)")
    public void removeGroupFromForm(@PathVariable long id, @RequestParam long grpid) {
        formService.removeGroupFromForm(id, grpid);
    }

    @DeleteMapping("/forms/{formid}/delete")
    @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') || @formSecurityService.isAuthor(authentication.name,#formid)")
    public void dropForm(@PathVariable long formid) {
        formService.dropForm(formid);
    }

    /* For Questions */

    // @GetMapping("/forms/{id}/questions")
    // @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') ||
    // @formSecurityService.isAuthor(authentication.name,#id) ||
    // @formSecurityService.canRespond(authentication.name,#id)")
    // public List<Question> getAllQuestions(@PathVariable long id) {
    // return questionService.getAllQuestions(id);
    // }

    @GetMapping("/forms/{formid}/questions")
    @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') || @formSecurityService.isAuthor(authentication.name,#formid) || @formSecurityService.canRespond(authentication.name,#formid)")
    public List<QuestionDTO> getAllQuestions(@PathVariable long formid) {
        return questionService.getAllQuestionsWithOptions(formid);
    }

    @GetMapping("/forms/{formid}/conditionals")
    @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') || @formSecurityService.isAuthor(authentication.name,#formid) || @formSecurityService.canRespond(authentication.name,#formid)")
    public List<ConditionalOrdering> getMethodName(@PathVariable long formid) {
        return conditionalService.getFormOrderings(formid);
    }

    // @PostMapping("/forms/{id}/addQ")
    // @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') ||
    // @formSecurityService.isAuthor(authentication.name,#id)")
    // public void addQuesToForm(@RequestBody Question newQ, @PathVariable long id)
    // {
    // questionService.addQuesToForm(newQ, id);
    // }

    @PostMapping("/forms/{formid}/addQ")
    @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') || @formSecurityService.isAuthor(authentication.name,#formid)")
    public void addQuesToForm(@RequestBody QuestionDTO newQ, @PathVariable long formid) {
        questionService.addQuesWithOptionsToForm(newQ, formid);
    }

    @DeleteMapping("/forms/{formid}/delete/{questionid}")
    @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') || @formSecurityService.isAuthor(authentication.name,#formid)")
    public void dropQues(@PathVariable long questionid, @PathVariable long formid) {
        questionService.dropQues(formid, questionid);
    }

    // @GetMapping("forms/{id}/users")
    // @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') ||
    // @formSecurityService.isAuthor(authentication.name,#id)")
    // // ! Might need stricter access
    // public List<UserDTO> getUsers(@PathVariable long id, @RequestParam(required =
    // false) Boolean responded) {
    // return formService.openToUsersDTO(id, responded);
    // }

    @GetMapping("/forms/{id}/users")
    @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') || @formSecurityService.isAuthor(authentication.name,#id)")
    public List<AbstractUserObj> getUsers(@PathVariable long id, @RequestParam(required = false) Boolean responded) {
        return (formService.openToUsersDTO(id, responded)).stream().map(AbstractUserObj::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/forms/{id}/responses")
    @PreAuthorize("hasRole('ROLE_MANAGE_FORMS') || @formSecurityService.isAuthor(authentication.name,#id)")
    public Map<Long, Map<String, Object>> getAllFormResponses(@PathVariable long id, @RequestParam(required = false, defaultValue = "false") boolean export) {
        // System.out.println("Request received: " + id);
        return formService.getAllFormResponses(id, export)
    }
    

}
