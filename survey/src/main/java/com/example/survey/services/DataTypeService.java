package com.example.survey.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.survey.data_access_layers.DataTypeRepository;
import com.example.survey.entities.DataType;

@Service
public class DataTypeService {

    @Autowired
    private DataTypeRepository dataTypeRepo;

    public List<DataType> getAllTypes() {
        return dataTypeRepo.getAllTypes();
    }

    public void addType(DataType newtype) {
        dataTypeRepo.addType(newtype);
    }

    public void dropType(int id) {
        dataTypeRepo.dropType(id);
    }

    public DataType findTypeById(int id) {
        return dataTypeRepo.findById(id);
    }

    public DataType updaDataType(DataType dataType) {
        return dataTypeRepo.updateDataType(dataType);
    }

}
