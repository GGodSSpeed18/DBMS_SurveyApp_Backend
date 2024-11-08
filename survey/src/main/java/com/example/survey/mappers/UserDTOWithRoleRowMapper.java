package com.example.survey.mappers;

import org.springframework.jdbc.core.RowMapper;

import com.example.survey.data_transfer_objects.UserDTO;
import com.example.survey.entities.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDTOWithRoleRowMapper implements RowMapper<UserDTO> {

    @Override
    public UserDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Instantiate a new UserDTO object
        UserDTO userDTO = new UserDTO();

        // Populate UserDTO fields
        userDTO.setUser_id(rs.getLong("user_id"));
        userDTO.setFirst_name(rs.getString("first_name"));
        userDTO.setMiddle_name(rs.getString("middle_name"));
        userDTO.setLast_name(rs.getString("last_name"));
        userDTO.setEmail(rs.getString("email"));
        userDTO.setDob(rs.getDate("dob"));
        userDTO.setGender(rs.getString("gender"));
        userDTO.setUser_role(rs.getInt("user_role"));

        // Map role data to a new Role object
        Role role = new Role();
        role.setRole_id(rs.getInt("role_id"));
        role.setRole_name(rs.getString("role_name"));
        role.setCan_respond(rs.getBoolean("can_respond"));
        role.setCan_author_form(rs.getBoolean("can_author_form"));
        role.setCan_author_group(rs.getBoolean("can_author_group"));
        role.setCan_manage_forms(rs.getBoolean("can_manage_forms"));

        // Assign the Role object to the UserDTO
        userDTO.setRole(role);

        return userDTO;
    }
}
