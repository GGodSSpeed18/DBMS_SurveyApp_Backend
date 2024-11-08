package com.example.survey.controllers;

import java.util.List;
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

import com.example.survey.data_transfer_objects.AbstractUserObj;
import com.example.survey.entities.Group;
import com.example.survey.entities.User;
import com.example.survey.services.GroupSecurityService;
import com.example.survey.services.GroupService;

@RestController
public class GroupController {
    @Autowired
    private GroupService groupService;
    @SuppressWarnings("unused")
    @Autowired
    private GroupSecurityService groupSecurityService;

    @GetMapping("/groups/{id}/users")
    @PreAuthorize("hasRole('ROLE_MANAGE_GROUPS') || @groupSecurityService.isAuthor(#id,authentication.name)")
    public List<AbstractUserObj> getUsersofGroup(@PathVariable long id) {
        return groupService.getUsersofGroup(id).stream().map(AbstractUserObj::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/groups")
    @PreAuthorize("hasRole('ROLE_MANAGE_GROUPS') || hasRole('ROLE_AUTHOR_GROUP')")
    public List<Group> getAllGroups() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (!currentUser.getRole().isCan_manage_groups()) {
            return groupService.getAllGroupsFromUser(currentUser.getUser_id());
        }
        return groupService.getAllGroups();
    }

    @GetMapping("/groups/{id}")
    @PreAuthorize("@groupSecurityService.isAuthor(#id,authentication.name) || @groupSecurityService.isMember(#id,authentication.name) || hasRole('ROLE_MANAGE_GROUPS')")
    public Group getGroupbyId(@PathVariable long id) {
        return groupService.getGroupbyId(id);
    }

    @PostMapping("/groups/{id}/modify")
    @PreAuthorize("@groupSecurityService.isAuthor(#id,authentication.name) || hasRole('ROLE_MANAGE_GROUPS')")
    public void modifyGroup(@RequestBody Group updgrp, @PathVariable long id) {
        groupService.modifyGroup(updgrp, id);
    }

    @PostMapping("/groups/{id}/adduser")
    @PreAuthorize("@groupSecurityService.isAuthor(#id,authentication.name) || hasRole('ROLE_MANAGE_GROUPS')")
    public void addUserToGroup(@RequestParam long userid, @PathVariable long id) {
        groupService.addUserToGroup(userid, id);
    }

    @PostMapping("/groups/{id}/addEmail")
    @PreAuthorize("@groupSecurityService.isAuthor(#id,authentication.name) || hasRole('ROLE_MANAGE_GROUPS')")
    public void addUserToGroupByEmail(@RequestParam String email, @PathVariable long id) {
        groupService.addUserToGroupByEmail(email, id);
    }

    @PostMapping("/groups/{id}/dropuser")
    @PreAuthorize("@groupSecurityService.isAuthor(#id,authentication.name) || hasRole('ROLE_MANAGE_GROUPS')")
    public void dropUserFromGroup(@RequestParam long userid, @PathVariable long id) {
        groupService.dropUserFromGroup(userid, id);
    }

    @PostMapping("/groups")
    @PreAuthorize("hasRole('ROLE_AUTHOR_GROUP')")
    public void createGroup(@RequestBody Group newgrp) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        newgrp.setGroup_owner(currentUser.getUser_id());
        groupService.createGroup(newgrp);
    }

    @DeleteMapping("/groups/{id}/delete")
    @PreAuthorize("@groupSecurityService.isAuthor(#id,authentication.name) || hasRole('ROLE_MANAGE_GROUPS')")
    public void dropGroup(@PathVariable long id) {
        groupService.dropGroup(id);
    }
}
