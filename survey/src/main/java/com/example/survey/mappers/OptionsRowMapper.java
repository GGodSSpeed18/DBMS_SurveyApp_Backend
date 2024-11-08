package com.example.survey.mappers;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.survey.data_transfer_objects.Option;

public class OptionsRowMapper implements RowMapper<Option> {

    @Override
    public Option mapRow(ResultSet rs, int rowNum) throws SQLException {
        Option option = new Option();

        // Map columns from the ResultSet to the Option object's fields
        option.setForm_id(rs.getLong("form_id"));
        option.setQuestion_id(rs.getLong("question_id"));
        option.setOption_id(rs.getLong("option_id"));

        // Map nullable fields
        option.setOption_int(rs.getObject("option_value_int") != null ? rs.getLong("option_value_int") : null);
        option.setOption_string(rs.getString("option_value_string"));
        option.setOption_float(rs.getObject("option_value_float") != null ? rs.getFloat("option_value_float") : null);

        // Map special_option as Boolean (from TINYINT in SQL)
        // option.setSpecial_option(rs.getBoolean("special_option"));

        return option;
    }
}
