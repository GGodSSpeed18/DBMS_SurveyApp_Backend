package com.example.survey.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.survey.data_access_layers.RoleRepository;
import com.example.survey.data_access_layers.UserRepository;
import com.example.survey.data_transfer_objects.UserDTO;
import com.example.survey.entities.Role;
import com.example.survey.entities.User;

@Service
public class RoleSecurityService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    public boolean isSubset(String email, int roleid) {
        UserDTO user = userRepository.findByEmailDTO(email);
        Role childRole = roleRepository.findRoleById(roleid);
        return roleRepository.isSubset(user.getRole(), childRole);
    }

    public boolean isRoleSubset(Role parentRole, Role childRole) {
        return roleRepository.isSubset(parentRole, childRole);
    }

    public List<Role> getSubsetRoles(User user) {
        return roleRepository.getSubsetRoles(user.getUser_role());
    }
}
