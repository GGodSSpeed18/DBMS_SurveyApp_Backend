package com.example.survey.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.survey.data_access_layers.GroupRepository;

@Service
public class GroupSecurityService {
    @Autowired
    private GroupRepository groupRepository;

    public Boolean isAuthor(long group_id, String email) {
        return groupRepository.isAuthor(group_id, email);
    }

    public Boolean isMember(long group_id, String email) {
        return groupRepository.isMember(group_id, email);
    }
}
