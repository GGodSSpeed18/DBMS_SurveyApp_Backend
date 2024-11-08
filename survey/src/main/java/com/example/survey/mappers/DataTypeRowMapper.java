package com.example.survey.mappers;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.example.survey.entities.DataType;

public class DataTypeRowMapper implements RowMapper<DataType> {
    @Override
    public DataType mapRow(ResultSet rs, int rowNum) throws SQLException {
        DataType dataType = new DataType();

        // Set fields from the ResultSet
        dataType.setType_id(rs.getInt("type_id"));
        dataType.setType_name(rs.getString("type_name"));
        dataType.setType_desc(rs.getString("type_desc"));
        dataType.setMax_val(rs.getObject("max_val", Long.class));  // Handles possible null values
        dataType.setMin_val(rs.getObject("min_val", Long.class));
        dataType.setUsesOptions(rs.getBoolean("uses_options"));

        // Convert `mapped_to` ENUM to integer and set it in the entity
        String mappedToEnum = rs.getString("mapped_to");
        dataType.setMappedTo(mappedToEnum);  // The conversion to integer is handled within the setter

        return dataType;
    }
}
