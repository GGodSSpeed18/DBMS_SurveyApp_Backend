package com.example.survey.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.survey.data_access_layers.UserRepository;

@Service
public class UserSecurityService {
    @Autowired
    private UserRepository userRepository;

    public boolean matchEmailToId(long userid, String email) {
        return userRepository.matchEmailToId(userid, email);
    }
}
