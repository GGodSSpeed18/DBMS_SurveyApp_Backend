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
public class RoleService {
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private UserRepository userRepository;

    public List<User> getUserbyRole(Integer roleid) {
        return roleRepo.getUserbyRole(roleid);
    }

    public List<UserDTO> getUserDTObyRole(Integer roleid) {
        return roleRepo.getUserDTObyRole(roleid);
    }

    public void addRole(Role newrole) {
        roleRepo.addRole(newrole);
    }

    public void dropRole(Integer roleid) {
        roleRepo.dropRole(roleid);
    }

    public List<Role> getAllRoles() {
        return roleRepo.getAllRoles();
    }


    public Role getRoleById(Integer role_id) {
        return roleRepo.findRoleById(role_id);
    }

    public Role getRoleByName(String name) {
        return roleRepo.findRoleByName(name);
    }

    public void addUserToRole(int roleid, String email) {
        User user = userRepository.findByEmail(email);
        userRepository.setUserRole(user.getUser_id(), roleid);
    }
}
