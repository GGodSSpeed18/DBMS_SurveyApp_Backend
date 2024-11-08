package com.example.survey.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.survey.data_transfer_objects.AbstractUserObj;

public class AbstractUserRowMapper implements RowMapper<AbstractUserObj> {

    @Override
    public AbstractUserObj mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Instantiate a new UserDTO object
        AbstractUserObj user = new AbstractUserObj();

        // Populate UserDTO fields
        user.setUser_id(rs.getLong("user_id"));
        user.setEmail(rs.getString("email"));
        user.setUser_role(rs.getInt("user_role"));
        return user;
    }
}
