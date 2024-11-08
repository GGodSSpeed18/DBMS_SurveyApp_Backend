package com.example.survey.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.survey.data_transfer_objects.QuestionDTO;
import com.example.survey.entities.DataType;

public class QuestionDataTypeRowMapper implements RowMapper<QuestionDTO> {
    @Override
    public QuestionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        QuestionDTO questionDTO = new QuestionDTO();

        // Mapping fields for QuestionDTO
        questionDTO.setForm_id(rs.getLong("form_id"));
        questionDTO.setQuestion_id(rs.getLong("question_id"));
        questionDTO.setData_type(rs.getInt("data_type"));
        questionDTO.setRequired(rs.getBoolean("required"));
        questionDTO.setPrompt(rs.getString("prompt"));
        questionDTO.setMin_val(rs.getLong("q_min_val"));
        questionDTO.setMax_val(rs.getLong("q_max_val"));
        questionDTO.setConditional(rs.getBoolean("conditional"));

        // Mapping fields for DataType
        DataType dataType = new DataType();
        dataType.setType_id(rs.getInt("type_id"));
        dataType.setType_name(rs.getString("type_name"));
        dataType.setType_desc(rs.getString("type_desc"));
        dataType.setMax_val(rs.getLong("d_max_val"));
        dataType.setMin_val(rs.getLong("d_min_val"));
        dataType.setMappedTo(rs.getString("mapped_to"));
        dataType.setUsesOptions(rs.getBoolean("uses_options"));

        // Setting DataType object in QuestionDTO
        questionDTO.setDataType(dataType);

        // Setting options to null as per your requirement
        questionDTO.setOptions(null);

        return questionDTO;
    }
}
