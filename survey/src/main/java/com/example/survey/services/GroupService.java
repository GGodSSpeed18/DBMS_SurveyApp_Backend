package com.example.survey.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.survey.data_access_layers.GroupRepository;
import com.example.survey.data_transfer_objects.UserDTO;
import com.example.survey.entities.Group;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepo;

    public List<UserDTO> getUsersofGroup(long id) {
        return groupRepo.getUsersofGroup(id);
    }

    public List<Group> getAllGroups() {
        return groupRepo.getAllGroups();
    }

    public List<Group> getAllGroupsFromUser(long userid) {
        return groupRepo.getAllGroupsFromUser(userid);
    }

    public void addUserToGroup(long userid, long id) {
        groupRepo.addUserToGroup(userid, id);
    }

    public void dropGroup(long id) {
        groupRepo.dropGroup(id);
    }

    public void createGroup(Group newgrp) {
        groupRepo.createGroup(newgrp);
    }

    public Group getGroupbyId(long id) {
        return groupRepo.getGroupbyId(id);
    }

    public void modifyGroup(Group updgrp, long id) {
        groupRepo.modifyGroup(updgrp, id);
    }

    public void dropUserFromGroup(long userid, long id) {
        groupRepo.dropUserFromGroup(userid, id);
    }

    public void addUserToGroupByEmail(String email, long grpid) {
        groupRepo.addUserToGroupByEmail(email, grpid);
    }
}
