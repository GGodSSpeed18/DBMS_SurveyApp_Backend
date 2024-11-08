package com.example.survey.data_access_layers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.survey.entities.DataType;
import com.example.survey.mappers.DataTypeRowMapper;
import com.example.survey.utilities.TableNames;

@Repository
public class DataTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<DataType> getAllTypes() {
        String sql = String.format("SELECT * FROM %s", TableNames.typeT);
        return jdbcTemplate.query(sql, new DataTypeRowMapper());
    }

    public void addType(DataType newtype) {
        String sql = String.format(
                "INSERT INTO %s (type_name,type_desc,max_val,min_val,mapped_to,uses_options) VALUES (?,?,?,?,?,?)",
                TableNames.typeT);

        if (newtype.getType_name() == null) {
            throw new IllegalArgumentException("Cannot have a null typename!");
        }

        jdbcTemplate.update(sql, new Object[] { newtype.getType_name(), newtype.getType_desc(),
                newtype.getMax_val(), newtype.getMin_val(), newtype.getMappedToString(), newtype.isUsesOptions() });
    }

    public void dropType(int id) {
        String sql = String.format("DELETE FROM %s WHERE type_id=?", TableNames.typeT);
        try {
            jdbcTemplate.update(sql, new Object[] { id });
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Invalid Data Type: " + id, e);
        }
    }

    public DataType findById(int id) {
        String sql = String.format("SELECT * FROM %s WHERE type_id=?", TableNames.typeT);
        try {
            return jdbcTemplate.queryForObject(sql, new DataTypeRowMapper(), new Object[] { id });
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Datatype with id " + id + " does not exist!");
        }
    }

    public DataType updateDataType(DataType newDataType) {
        DataType updatedDataType = findById(newDataType.getType_id());
        if (newDataType.getType_name() != null) {
            updatedDataType.setType_name(newDataType.getType_name());
        }
        if (newDataType.getType_desc() != null) {
            updatedDataType.setType_desc(newDataType.getType_desc());
        }
        if (newDataType.getMax_val() != null) {
            updatedDataType.setMax_val(newDataType.getMax_val());
        }
        if (newDataType.getMin_val() != null) {
            updatedDataType.setMin_val(newDataType.getMin_val());
        }
        updatedDataType.setMappedTo(newDataType.getMappedTo());
        updatedDataType.setUsesOptions(newDataType.isUsesOptions());
        String sql = "UPDATE dataTypes SET type_name = ?, type_desc = ?, max_val = ?, min_val = ?, mapped_to = ?, uses_options = ? WHERE type_id = ?";

        jdbcTemplate.update(sql,
                updatedDataType.getType_name(),
                updatedDataType.getType_desc(),
                updatedDataType.getMax_val(),
                updatedDataType.getMin_val(),
                updatedDataType.getMappedToString(),
                updatedDataType.isUsesOptions(),
                updatedDataType.getType_id());

        return updatedDataType;
    }
}
