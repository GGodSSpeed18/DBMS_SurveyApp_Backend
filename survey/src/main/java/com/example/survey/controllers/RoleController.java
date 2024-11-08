package com.example.survey.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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

import com.example.survey.data_transfer_objects.UserDTO;
import com.example.survey.entities.Role;
import com.example.survey.entities.User;
import com.example.survey.services.RoleSecurityService;
import com.example.survey.services.RoleService;

@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;
    @SuppressWarnings("Unused")
    @Autowired
    private RoleSecurityService roleSecurityService;

    @GetMapping("/roles/{id}/users")
    @PreAuthorize("hasRole('ROLE_MANAGE_ROLES') && @roleSecurityService.isSubset(authentication.name,#id)")
    public List<UserDTO> getUsersByRole(@PathVariable Integer id) {
        return roleService.getUserDTObyRole(id);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ROLE_MANAGE_ROLES')")
    public List<Role> getAllRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return roleSecurityService.getSubsetRoles((User) authentication.getPrincipal());
    }

    @GetMapping("/roles/id")
    public Role getMethodName(@RequestParam Integer roleid) {
        return roleService.getRoleById(roleid);
    }

    @GetMapping("/roles/name")
    public Role getMethodName(@RequestParam String name) {
        return roleService.getRoleByName(name);
    }

    @PostMapping("/roles")
    @PreAuthorize("hasRole('ROLE_MANAGE_ROLES')")
    public void addRole(@RequestBody Role newrole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (!roleSecurityService.isRoleSubset(user.getRole(), newrole)) {
            throw new AccessDeniedException("You can only create roles with less than or equal privilege to yourself.");
        }
        roleService.addRole(newrole);
    }

    @DeleteMapping("roles/delete")
    @PreAuthorize("hasRole('ROLE_MANAGE_ROLES') && @roleSecurityService.isSubset(authentication.name,#id)")
    public void dropRole(@RequestParam Integer roleid) {
        roleService.dropRole(roleid);
    }

    @PostMapping("/roles/{roleid}/add")
    public void addUserToRole(@RequestParam String email, @PathVariable int roleid) {
        roleService.addUserToRole(roleid, email);
    }

}
