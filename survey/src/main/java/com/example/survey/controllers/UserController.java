package com.example.survey.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.survey.data_transfer_objects.AbstractUserObj;
import com.example.survey.data_transfer_objects.CompleteResponse;
import com.example.survey.data_transfer_objects.FormResponse;
import com.example.survey.data_transfer_objects.Notification;
import com.example.survey.data_transfer_objects.UserDTO;
import com.example.survey.entities.Form;
import com.example.survey.entities.Group;
import com.example.survey.entities.User;
import com.example.survey.services.NotificationService;
import com.example.survey.services.UserSecurityService;
import com.example.survey.services.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @SuppressWarnings("unused")
    @Autowired
    private UserSecurityService userSecurityService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/users/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO currentUser = new UserDTO((User) authentication.getPrincipal());
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_MANAGE_USERS')")
    public List<UserDTO> getUsers() {
        return userService.getAllDTO();
    }

    // @GetMapping("/users")
    // @PreAuthorize("hasRole('ROLE_MANAGE_USERS')")
    // public List<AbstractUserObj> getUsers() {
    // return userService.getAllDTO().stream().map(AbstractUserObj::new)
    // .collect(Collectors.toList());
    // }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGE_USERS') || @userSecurityService.matchEmailToId(#id,authentication.name)")
    public UserDTO getUserbyId(@PathVariable long id) {
        return userService.getUserbyIdDTO(id);
    }

    @PostMapping("/users")
    public void createUser(@RequestBody User newuser) {
        userService.createUser(newuser);
    }

    @PostMapping("/users/{userid}/setrole")
    @PreAuthorize("hasRole('ROLE_MANAGE_ROLES')")
    public void setUserRole(@PathVariable long userid, @RequestParam Integer roleid) {
        userService.setRole(userid, roleid);
    }

    @DeleteMapping("/users/{userid}/delete")
    @PreAuthorize("hasRole('ROLE_MANAGE_USERS')")
    public void dropUser(@PathVariable long userid) {
        userService.dropUser(userid);
    }

    // ! Depricated
    @PutMapping("/users/update")
    @PreAuthorize("isAuthenticated()")
    public UserDTO updateUser(@RequestBody User newUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getUser_id() != newUser.getUser_id()) {
            throw new AccessDeniedException("You are not authorized to do that!");
        }
        return userService.updateUserDTO(newUser);
    }

    @PutMapping("/users/{userid}/update")
    @PreAuthorize("hasRole('ROLE_MANAGE_USERS') || @userSecurityService.matchEmailToId(#userid,authentication.name)")
    public UserDTO updateUserById(@PathVariable long userid, @RequestBody User newUser) {
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // User currentUser = (User) authentication.getPrincipal();
        // if (currentUser.getUser_id() != newUser.getUser_id()) {
        // throw new AccessDeniedException("You are not authorized to do that!");
        // }
        newUser.setUser_id(userid);
        return userService.updateUserDTO(newUser);
    }

    @GetMapping("/users/{userid}/groups")
    @PreAuthorize("hasRole('ROLE_MANAGE_USERS') || @userSecurityService.matchEmailToId(#userid,authentication.name)")
    public List<Group> getUserGroups(@PathVariable long userid) {
        return userService.getGroups(userid);
    }

    @GetMapping("/users/{userid}/forms")
    @PreAuthorize("hasRole('ROLE_MANAGE_USERS') || @userSecurityService.matchEmailToId(#userid,authentication.name)")
    public List<Form> getUserForms(@PathVariable long userid, @RequestParam(required = false) Boolean active) {
        return userService.getForms(userid, active);
    }

    @GetMapping("/users/{userid}/responses")
    @PreAuthorize("hasRole('ROLE_MANAGE_USERS') || @userSecurityService.matchEmailToId(#userid,authentication.name)")
    public List<Form> getResponses(@PathVariable long userid) {
        return userService.getResponses(userid);
    }

    @PostMapping("/users/respond")
    @PreAuthorize("hasRole('ROLE_RESPOND')")
    public ResponseEntity<?> formRespond(@RequestBody FormResponse formResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (!userService.canRespondToForm(currentUser.getEmail(), formResponse.getForm_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to respond to this form");
        }
        formResponse.setUser_id(currentUser.getUser_id());
        userService.formRespond(formResponse);
        return ResponseEntity.ok("Form response submitted successfully");
    }

    @PostMapping("/users/respond/complete")
    @PreAuthorize("hasRole('ROLE_RESPOND')")
    public ResponseEntity<?> postMethodName(@RequestBody CompleteResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (!userService.canRespondToForm(currentUser.getEmail(), response.getMetadata().getForm_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to respond to this form");
        }
        response.getMetadata().setUser_id(currentUser.getUser_id());
        userService.completeFormRespond(response);
        return ResponseEntity.ok("Form response submitted successfully");
    }

    // notifications
    @GetMapping("/users/me/notifications")
    @PreAuthorize("hasRole('ROLE_RESPOND')")
    public List<Notification> getUserNotifs(@RequestParam(required = false) Boolean read) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AbstractUserObj currentUser = new AbstractUserObj((User) authentication.getPrincipal());
        return notificationService.getUserNotifs(currentUser.getUser_id(), read);
    }

    @PostMapping("/users/me/notifications")
    @PreAuthorize("isAuthenticated()")
    public void markAsRead(@RequestBody long notif_id) {
        notificationService.markAsRead(notif_id);
    }

    @GetMapping("/users/{id}/abstract")
    @PreAuthorize("isAuthenticated()")
    public AbstractUserObj getAbstractUser(@PathVariable long id) {
        return new AbstractUserObj(userService.getUserbyIdDTO(id));
    }

    @GetMapping("/users/findEmail")
    @PreAuthorize("hasAnyRole('ROLE_AUTHOR_FORM', 'ROLE_AUTHOR_GROUP', 'ROLE_MANAGE_GROUPS', 'ROLE_MANAGE_FORMS', 'ROLE_MANAGE_ROLES', 'ROLE_MANAGE_USERS')")
    public List<AbstractUserObj> findUserByEmailMatch(@RequestParam String email) {
        return userService.findUserByEmailMatch(email);
    }
    
    @GetMapping("/users/{userid}/forms_state")
    @PreAuthorize("hasRole('ROLE_MANAGE_USERS') || @userSecurityService.matchEmailToId(#userid,authentication.name)")
    public List<Form> getMethodName(@PathVariable long userid, @RequestParam(required = false) Boolean responded) {
        return userService.findFormsByUserResponded(userid, responded);
    }
    
}
