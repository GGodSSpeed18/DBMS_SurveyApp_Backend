package com.example.survey.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.survey.data_access_layers.RoleRepository;
import com.example.survey.data_access_layers.UserRepository;
import com.example.survey.data_transfer_objects.AbstractUserObj;
import com.example.survey.data_transfer_objects.CompleteResponse;
import com.example.survey.data_transfer_objects.FormResponse;
import com.example.survey.data_transfer_objects.UserDTO;
import com.example.survey.entities.Form;
import com.example.survey.entities.Group;
import com.example.survey.entities.Role;
import com.example.survey.entities.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    // private static RoleRepository roleRepository = new RoleRepository(new
    // JdbcTemplate());

    // public static List<GrantedAuthority> getAuthorities(User user) {
    // Role role = roleRepository.findRoleById(user.getUser_role());
    // List<GrantedAuthority> authorities = new ArrayList<>();

    // if (role.isCan_respond()) {
    // authorities.add(new SimpleGrantedAuthority("ROLE_RESPOND"));
    // }
    // if (role.isCan_author_form()) {
    // authorities.add(new SimpleGrantedAuthority("ROLE_AUTHOR_FORM"));
    // }
    // if (role.isCan_author_group()) {
    // authorities.add(new SimpleGrantedAuthority("ROLE_AUTHOR_GROUP"));
    // }
    // if (role.isCan_manage_forms()) {
    // authorities.add(new SimpleGrantedAuthority("ROLE_MANAGE_FORMS"));
    // }

    // return authorities;
    // }

    @Autowired
    private RoleRepository roleRepository;

    public Boolean canRespondToForm(String email, long formid) {
        return userRepo.canRespondToForm(email, formid);
    }

    public User getUserbyId(long userid) {
        return userRepo.findById(userid);
    }

    public UserDTO getUserbyIdDTO(long userid) {
        return userRepo.findByIdDTO(userid);
    }

    public User loadUserWithRoleObject(Long userid) {
        User user = userRepo.findById(userid);
        Role role = roleRepository.findRoleById(user.getUser_role());
        user.setRole(role);
        return user;
    }

    public UserDTO loadUserWithRoleObjectDTO(Long userid) {
        UserDTO userDTO = userRepo.findByIdDTO(userid);
        Role role = roleRepository.findRoleById(userDTO.getUser_role());
        userDTO.setRole(role);
        return userDTO;
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public List<UserDTO> getAllDTO() {
        return userRepo.findAllDTO();
    }

    public void setRole(long userid, Integer roleid) {
        userRepo.setUserRole(userid, roleid);
    }

    public void createUser(User newuser) {
        userRepo.createUser(newuser);
    }

    public void dropUser(long userid) {
        userRepo.dropUser(userid);
    }

    public User updateUser(User newUser) {
        return userRepo.updateUser(newUser);
    }

    public UserDTO updateUserDTO(User newUser) {
        return userRepo.updateUserDTO(newUser);
    }

    public List<Group> getGroups(long userid) {
        return userRepo.findGroupsByUser(userid);
    }

    public List<Form> getForms(long userid, Boolean active) {
        return userRepo.findFormsByUser(userid, active);
    }

    public List<Form> getResponses(long userid) {
        return userRepo.findResponses(userid);
    }

    public void formRespond(FormResponse response) {
        userRepo.formRespond(response);
    }

    public void completeFormRespond(CompleteResponse response) {
        userRepo.completeFormRespond(response);
    }

    public List<AbstractUserObj> findUserByEmailMatch(String email) {
        return userRepo.findUserByEmailMatch(email);
    }

    public List<Form> findFormsByUserResponded(long userid, Boolean resp) {
        return userRepo.findFormsByUserResponded(userid, resp);
    }
    // public List<Form> getForms(long userid, boolean active) {
    // return userRepo.findActiveFormsForUser(userid, active);
    // }
}
