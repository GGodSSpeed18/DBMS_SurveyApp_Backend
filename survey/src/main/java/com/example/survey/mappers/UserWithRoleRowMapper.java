package com.example.survey.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.survey.entities.Role;
import com.example.survey.entities.User;

public class UserWithRoleRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUser_id(rs.getLong("user_id"));
        user.setFirst_name(rs.getString("first_name"));
        user.setMiddle_name(rs.getString("middle_name"));
        user.setLast_name(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone_code(rs.getInt("phone_code"));
        user.setPhone_number(rs.getString("phone_number"));
        user.setDob(rs.getDate("dob"));
        user.setGender(rs.getString("gender"));
        user.setPasskey(rs.getString("passkey"));
        user.setUser_role(rs.getInt("user_role"));

        // Map the role
        Role role = new Role();
        role.setRole_id(rs.getInt("role_id"));
        role.setRole_name(rs.getString("role_name"));
        role.setCan_respond(rs.getBoolean("can_respond"));
        role.setCan_author_form(rs.getBoolean("can_author_form"));
        role.setCan_author_group(rs.getBoolean("can_author_group"));
        role.setCan_manage_forms(rs.getBoolean("can_manage_forms"));
        role.setCan_manage_groups(rs.getBoolean("can_manage_groups"));
        role.setCan_manage_roles(rs.getBoolean("can_manage_roles"));
        role.setCan_manage_users(rs.getBoolean("can_manage_users"));

        // Assign the role to the user
        user.setRole(role);

        return user;
    }
}
